package com.github.b1412.user.event

import org.springframework.context.ApplicationEvent

data class PasswordRecoveryEvent(val source: Map<String, String>) : ApplicationEvent(source)