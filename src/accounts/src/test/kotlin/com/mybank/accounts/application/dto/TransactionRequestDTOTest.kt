package com.mybank.accounts.application.dto

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.util.Assert
import java.math.BigDecimal
import java.util.*
import kotlin.test.assertFailsWith

class TransactionRequestDTOTest {
    @Test
    fun `should valid when params is ok`(){
        val accountId = UUID.randomUUID()
        val type = 'C'
        val amount = BigDecimal.TEN
        val description = "Teste"

        val request = TransactionRequestDTO(accountId, type, amount, description)

        Assert.notNull(request, "Valid request!")
        Assertions.assertEquals(accountId, request.accountId)
        Assertions.assertEquals(type, request.type)
        Assertions.assertEquals(amount, request.amount)
        Assertions.assertEquals(description, request.description)
    }

    @Test
    fun `should invalid when type not is C or D`(){
        val accountId = UUID.randomUUID()
        val type = 'F'
        val amount = BigDecimal.TEN
        val description = "Teste"


        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = TransactionRequestDTO(accountId, type, amount, description)
            }
        )
    }

    @Test
    fun `should invalid when amount is less than or equals ZERO`(){
        val accountId = UUID.randomUUID()
        val type = 'D'
        val amount = BigDecimal.ZERO
        val description = "Teste"


        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = TransactionRequestDTO(accountId, type, amount, description)
            }
        )
    }

    @Test
    fun `should invalid when description is empty`(){
        val accountId = UUID.randomUUID()
        val type = 'D'
        val amount = BigDecimal.TEN
        val description = ""


        assertFailsWith<IllegalArgumentException>(
            block = {
                val request = TransactionRequestDTO(accountId, type, amount, description)
            }
        )
    }
}