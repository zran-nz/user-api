package com.github.b1412.user.controller

import com.github.b1412.user.controller.base.BasePasswordRecoveryController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/password-recovery")
class PasswordRecoveryController : BasePasswordRecoveryController()