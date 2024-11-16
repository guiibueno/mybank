package com.mybank.accounts.infraestructure.adapters.input.rest

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.port.input.GetAccountPort
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.util.*
import kotlin.test.Test

class AccountsRestAdapterTest {
    private var accountsRestAdapter: AccountsRestAdapter

    @MockK
    private lateinit var getAccountPort: GetAccountPort

    init {
        MockKAnnotations.init(this)
        accountsRestAdapter = spyk(
            AccountsRestAdapter(getAccountPort)
        )
    }

    @Test
    fun `should returns 200 when account exists`() {
        val accountId = UUID.randomUUID()

        val accountMock = AccountDTO(accountId, BigDecimal.ZERO)

        every { getAccountPort.invoke(any<UUID>()) } returns accountMock

        val response = accountsRestAdapter.getById(accountId)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(HttpStatus.OK,response.statusCode)
        Assertions.assertEquals(accountId, response.body?.id)
        Assertions.assertEquals(accountMock.balance, response.body?.balance)

        coVerify (exactly = 1) {
            getAccountPort.invoke(any())
        }
    }

    @Test
    fun `should returns 404 when account not exists`() {
        val accountId = UUID.randomUUID()

        every { getAccountPort.invoke(any<UUID>()) } returns null

        val response = accountsRestAdapter.getById(accountId)

        Assertions.assertNotNull(response)
        Assertions.assertNull(response.body)
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.statusCode)

        coVerify (exactly = 1) {
            getAccountPort.invoke(any())
        }
    }
}