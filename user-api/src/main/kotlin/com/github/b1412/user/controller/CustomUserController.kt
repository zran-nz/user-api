package com.github.b1412.user.controller

import com.github.b1412.email.service.EmailLogService
import com.github.b1412.email.service.EmailTemplateService
import com.github.b1412.encrypt.DESUtil
import com.github.b1412.error.ErrorDTO
import com.github.b1412.extenstions.responseEntityOk
import com.github.b1412.json.GraphRender
import com.github.b1412.permission.dao.BranchDao
import com.github.b1412.permission.dao.RoleDao
import com.github.b1412.permission.entity.User
import com.github.b1412.permission.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/v1/user")
class CustomUserController(
        @Autowired
        val userService: UserService,
        @Autowired
        val passwordEncoder: PasswordEncoder,
        @Autowired
        val emailTemplateService: EmailTemplateService,
        @Autowired
        val emailLogService: EmailLogService,
        @Autowired
        val roleDao: RoleDao,
        @Autowired
        val branchDao: BranchDao
) {
    @GraphRender("user")
    @PostMapping("/register")
    @Transactional
    fun register(@Validated @RequestBody user: User, request: HttpServletRequest, b: UriComponentsBuilder): ResponseEntity<*> {
        if (user.password != user.confirmPassword) {
            return ResponseEntity.badRequest().body(ErrorDTO(message = "password not equal"))
        }
        val clientId = "4"
        user.setUsername(user.email!!)
        user.setPassword(passwordEncoder.encode(user.password))
        user.active = false
        user.clientId = clientId

        val role = roleDao.findByIdOrNull(user.role!!.id)
        val branch = branchDao.findByIdOrNull(1L)
        user.role = role
        user.branch = branch
        userService.save(user)
        val id = DESUtil.encrypt(user.id.toString(), KEY)
        val model = mutableMapOf(
                "url" to "http://dev.domain.com/#/pages/email-active/$id",
                "username" to user.username!!
        )
        emailTemplateService.send("User Register", user.email!!, model)
        emailLogService.execute()
        val uriComponents = b.path("/v1/user/{id}").buildAndExpand(user.id)
        val headers = HttpHeaders()
        headers.location = uriComponents.toUri()
        return ResponseEntity.created(uriComponents.toUri()).build<Void>()
    }

    @GraphRender("user")
    @GetMapping("/me")
    fun me(): ResponseEntity<*> {
        val me = SecurityContextHolder.getContext().authentication.principal as User
        return me.responseEntityOk()
    }

    @GraphRender("user")
    @PostMapping("/active/{id}")
    fun active(@PathVariable id: String): ResponseEntity<*> {
        val me = userService.findByIdOrNull(DESUtil.decrypt(id, KEY).toLong())!!
        me.active = true
        userService.save(me)
        return ResponseEntity.noContent().build<Void>()
    }

    companion object {
        const val KEY = "aaasssdd"
    }
}