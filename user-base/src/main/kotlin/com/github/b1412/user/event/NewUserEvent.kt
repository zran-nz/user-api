package com.github.b1412.user.event

import org.springframework.context.ApplicationEvent

class NewUserEvent(userId: Long, source: Map<String, String>, val action: String) : ApplicationEvent(source)