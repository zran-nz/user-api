package com.github.b1412.user.excel

import com.github.b1412.user.entity.PasswordRecovery
import com.github.b1412.excel.service.ExcelParsingRule
import com.github.b1412.files.parser.FileParser
import com.github.b1412.excel.convertor.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.EntityManager


@Component
class PasswordRecoveryExcelParsingRule(
        @Autowired
        val entityManager: EntityManager

) : ExcelParsingRule<PasswordRecovery> {

    override val fileParser: FileParser
    get() {
        val fileParser = FileParser()
        fileParser.start = 1
    fileParser.addCell(1, "username")
    fileParser.addCell(2, "email")
    fileParser.addCell(3, "expireDate")
    fileParser.addCell(4, "used")
    fileParser.addCell(5, "encryptId")
    fileParser.addCell(6, "id", LongConvertor())
    fileParser.addCell(7, "version", LongConvertor())
    fileParser.addCell(8, "createdAt")
    fileParser.addCell(9, "updatedAt")
    fileParser.addCell(10, "deletedAt")
        fileParser.addCell(2, "category", EntityConvertor().apply {
                name = "Creator"
                em = entityManager
        })
        fileParser.addCell(2, "category", EntityConvertor().apply {
                name = "Modifier"
                em = entityManager
        })
        return fileParser
    }

    override val entityClass: Class<*>
    get() = PasswordRecovery::class.java

    override val ruleName: String
    get() = "passwordRecovery"

    override fun process(data: List<PasswordRecovery>) {
        data.forEach{
            entityManager.persist(it)
        }
    }
}
