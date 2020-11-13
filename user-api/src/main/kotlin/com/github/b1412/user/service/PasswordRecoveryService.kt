package com.github.b1412.user.service

import com.github.b1412.api.service.BaseService
import com.github.b1412.user.dao.PasswordRecoveryDao
import com.github.b1412.user.entity.PasswordRecovery
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class PasswordRecoveryService(
        @Autowired
        val dao: PasswordRecoveryDao
) : BaseService<PasswordRecovery, Long>(dao = dao)
