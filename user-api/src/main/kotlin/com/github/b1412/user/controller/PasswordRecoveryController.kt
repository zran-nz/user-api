package com.github.b1412.user.controller

import arrow.core.Either.Right
import arrow.core.Either.Left
import com.github.b1412.cache.CacheClient
import com.github.b1412.email.service.EmailLogService
import com.github.b1412.email.service.EmailTemplateService
import com.github.b1412.encrypt.DESUtil
import com.github.b1412.error.ErrorDTO
import com.github.b1412.extenstions.responseEntityOk
import com.github.b1412.permission.dao.UserDao
import com.github.b1412.user.controller.base.BasePasswordRecoveryController
import com.github.b1412.user.dao.PasswordRecoveryDao
import com.github.b1412.user.entity.PasswordRecovery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.ZonedDateTime

@RestController
@RequestMapping("/v1/password-recovery")
class PasswordRecoveryController(
        @Value("\${spring.application.name}")
        val application: String,
        @Autowired
        val cacheClient: CacheClient,
        @Autowired
        val findPwdSendLogDao: PasswordRecoveryDao,
        @Autowired
        val userDao: UserDao,
        @Autowired
        val passwordEncoder: PasswordEncoder,
        @Autowired
        val emailTemplateService: EmailTemplateService,
        @Autowired
        val emailLogService: EmailLogService
) : BasePasswordRecoveryController() {
    @PostMapping("apply/{username}")
    @ResponseBody
    fun apply(@PathVariable username: String): ResponseEntity<*> {
        return when (val userOpt = userDao.searchOneBy(mapOf("username_eq" to username))) {
            is Left -> ResponseEntity.badRequest().body(ErrorDTO(message = "invalid username"))
            is Right -> {
                val user = userOpt.b
                val log = PasswordRecovery(
                        username = username,
                        email = user.email,
                        used = false,
                        expireDate = ZonedDateTime.now().plusDays(1)
                )
                findPwdSendLogDao.save(log)
                val encryptId = DESUtil.encrypt(log.id!!.toString(), KEY)
                log.encryptId = encryptId
                findPwdSendLogDao.save(log)

                val model = mutableMapOf(
                        "url" to "http://dev.domain.com/#/pages/password-recovery/$encryptId"
                )
                emailTemplateService.send("Password Recovery", user.email!!, model)
                emailLogService.execute()
                ResponseEntity.noContent().build<Void>()
            }
        }
    }

    @GetMapping("/status/{encryptId}")
    fun getByEncryptId(@PathVariable encryptId: String): ResponseEntity<Map<String, Any?>> {
        val decryptedId = DESUtil.decrypt(encryptId, KEY)
        val log = findPwdSendLogDao.findByIdOrNull(decryptedId.toLong())!!
        val isExpired = ZonedDateTime.now().isAfter(log.expireDate)
        val map = mapOf(
                "expired" to isExpired,
                "used" to log.used
        )
        return map.responseEntityOk()
    }


    @PostMapping("reset/{encryptId}")
    @ResponseBody
    fun resetPwd(@PathVariable encryptId: String, newPassword: String, confirmPassword: String): ResponseEntity<*> {
        val decryptedId = DESUtil.decrypt(encryptId, KEY)
        val log = findPwdSendLogDao.findByIdOrNull(decryptedId.toLong())!!
        val username = log.username!!
        if (log.used!!) {
            return ResponseEntity.badRequest().body(ErrorDTO(message = "link used"))
        }
        return when (val userOpt = userDao.searchOneBy(mapOf("username_eq" to username))) {
            is Left -> ResponseEntity.badRequest().body(ErrorDTO(message = "invalid username"))
            is Right -> {
                val user = userOpt.b
                if (newPassword != confirmPassword) {
                    return ResponseEntity.badRequest().body(ErrorDTO(message = "password doesn't equal"))
                }
                user.setPassword(passwordEncoder.encode(newPassword))
                userDao.save(user)
                log.used = true
                findPwdSendLogDao.save(log)
                cacheClient.deleteByPattern("*$application-${user.username}*".toLowerCase())
                ResponseEntity.noContent().build<Void>()
            }
        }
    }

    companion object {
        const val KEY = "aaasssdd"
    }
}