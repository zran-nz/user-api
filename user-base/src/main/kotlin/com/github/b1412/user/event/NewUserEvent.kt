package com.github.b1412.user.event

import org.springframework.context.ApplicationEvent

data class NewUserEvent(val source: Map<String, String>, val action: NewUserAction) : ApplicationEvent(source)