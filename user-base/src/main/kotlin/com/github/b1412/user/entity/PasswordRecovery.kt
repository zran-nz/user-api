package com.github.b1412.user.entity


import com.github.b1412.api.entity.BaseEntity
import com.github.b1412.generator.metadata.EntityFeature
import org.hibernate.annotations.Type
import java.time.ZonedDateTime
import javax.persistence.Entity

@EntityFeature
@Entity
data class PasswordRecovery(
    var username: String? = null,
    var email: String? = null,
    var expireDate: ZonedDateTime? = null,
    @Type(type = "yes_no")
    var used: Boolean? = null,
    var encryptId: String? = null
) : BaseEntity()
