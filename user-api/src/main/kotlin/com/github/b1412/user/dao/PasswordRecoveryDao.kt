package com.github.b1412.user.dao

import com.github.b1412.api.dao.BaseDao
import com.github.b1412.user.entity.PasswordRecovery
import org.springframework.stereotype.Repository

@Repository
interface PasswordRecoveryDao : BaseDao<PasswordRecovery, Long>
