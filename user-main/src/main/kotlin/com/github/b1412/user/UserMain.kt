package com.github.b1412.user

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication(
        scanBasePackages = ["com.github.b1412.*"],
        exclude = [
            SecurityAutoConfiguration::class,
            ApplicationAvailabilityAutoConfiguration::class]
)
@EntityScan("com.github.b1412.*")
class UserApp

fun main(args: Array<String>) {
    runApplication<UserApp>(*args)
}
