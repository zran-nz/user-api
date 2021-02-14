package com.github.b1412.user.controller.custom

import com.github.b1412.cache.CacheClient
import com.github.b1412.encrypt.DESUtil
import com.github.b1412.error.ErrorDTO
import com.github.b1412.extenstions.responseEntityOk
import com.github.b1412.generator.metadata.PermissionFeature
import com.github.b1412.generator.metadata.PermissionFeatureIgnore
import com.github.b1412.generator.metadata.PermissionFeatures
import com.github.b1412.json.GraphRender
import com.github.b1412.permission.dao.BranchDao
import com.github.b1412.permission.dao.RoleDao
import com.github.b1412.permission.entity.User
import com.github.b1412.permission.service.UserService
import com.github.b1412.user.event.NewUserAction
import com.github.b1412.user.event.NewUserEvent
import org.hibernate.validator.constraints.Length
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Transactional
@RestController
@RequestMapping("/v1/user")
class UserControllerCustom(
    val userService: UserService,
    val passwordEncoder: PasswordEncoder,
    val roleDao: RoleDao,
    val branchDao: BranchDao,
    val cacheClient: CacheClient,
    val applicationEventPublisher: ApplicationEventPublisher,
    @Value("\${spring.application.name}")
    val application: String,
) {
    @PermissionFeatureIgnore
    @PostMapping("/register")
    fun register(
        @Validated @RequestBody user: User,
        @RequestParam filter: Map<String, String>,
        b: UriComponentsBuilder

    ): ResponseEntity<*> {
        //TODO

        //       if (user.password != user.confirmPassword) {
        //           return ResponseEntity.badRequest().body(ErrorDTO(message = "password not equal"))
        //       }

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
        val encryptedId = DESUtil.encrypt(user.id.toString(), KEY)
        val action = filter["action"]
        val e = if (action == null) {
            NewUserAction.REGISTRATION
        } else {
            NewUserAction.valueOf(action)
        }

        applicationEventPublisher.publishEvent(
            NewUserEvent(
                filter + Pair("userId", encryptedId),
                e
            )
        )

        val uriComponents = b.path("{id}").buildAndExpand(encryptedId)
        return ResponseEntity.created(uriComponents.toUri()).build<Void>()
    }


    @PostMapping("/create")
    @PermissionFeatures(
        PermissionFeature(role = "admin", rule = "all")
    )
    fun create(
        @Validated @RequestBody user: User,
        request: HttpServletRequest,
        b: UriComponentsBuilder
    ): ResponseEntity<*> {
        val clientId = "4"
        user.setUsername(user.email!!)
        user.setPassword(passwordEncoder.encode(user.password))
        user.active = true
        user.clientId = clientId

        val role = roleDao.findByIdOrNull(user.role!!.id)
        val branch = branchDao.findByIdOrNull(1L)
        user.role = role
        user.branch = branch
        userService.save(user)
        return ResponseEntity.created(b.path("/v1/user/{id}").buildAndExpand(user.id).toUri()).build<Void>()
    }

    @GetMapping("/me")
    @GraphRender("user")
    @PermissionFeatures(
        PermissionFeature(role = "admin", rule = "all"),
        PermissionFeature(role = "app admin", rule = "all"),
        PermissionFeature(role = "teaching staff", rule = "all"),
        PermissionFeature(role = "student", rule = "all")
    )
    fun me(): ResponseEntity<*> {
        val me = SecurityContextHolder.getContext().authentication.principal as User
        return me.responseEntityOk()
    }

    @PermissionFeatureIgnore
    @PostMapping("/active/{id}")
    fun active(@PathVariable id: String): ResponseEntity<*> {
        val me = userService.findByIdOrNull(DESUtil.decrypt(id, KEY).toLong())!!
        me.active = true
        userService.save(me)
        return ResponseEntity.noContent().build<Void>()
    }

    @PatchMapping("/password")
    @PermissionFeatures(
        PermissionFeature(role = "admin", rule = "all"),
        PermissionFeature(role = "app admin", rule = "all"),
        PermissionFeature(role = "teaching staff", rule = "all"),
        PermissionFeature(role = "student", rule = "all")
    )
    fun changePassword(@Valid @RequestBody passwordChange: PasswordChange): ResponseEntity<*> {
        if (passwordChange.newPassword != passwordChange.confirmPassword) {
            return ResponseEntity.badRequest().body(ErrorDTO(message = "newPassword and confirmPassword not equal"))
        }

        var user = SecurityContextHolder.getContext().authentication.principal as User
        // user = userService.findByIdOrNull(user.id)!!
        user = userService.searchOneBy(mapOf("username_eq" to user.username!!)).orNull()!!
        if (passwordEncoder.matches(passwordChange.oldPassword, user.password).not()) {
            return ResponseEntity.badRequest().body(ErrorDTO(message = "oldPassword not correct"))
        }
        user.setPassword(passwordEncoder.encode(passwordChange.newPassword))
        userService.save(user)
        cacheClient.deleteByPattern("*$application-${user.username}*".toLowerCase())
        return ResponseEntity.ok().build<Void>()
    }

    data class PasswordChange(
        @Length(min = 8, max = 32) val newPassword: String? = null,
        @NotEmpty val oldPassword: String? = null,
        @NotEmpty val confirmPassword: String? = null
    )

    companion object {
        const val KEY = "aaasssdd"
    }
}