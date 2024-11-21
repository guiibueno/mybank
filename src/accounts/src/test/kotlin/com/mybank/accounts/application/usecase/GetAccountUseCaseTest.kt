package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.AccountOutputPort
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class GetAccountUseCaseTest {
    private var getAccountUseCase: GetAccountUseCase

    @MockK
    private lateinit var accountOutputPort: AccountOutputPort

    init {
        MockKAnnotations.init(this)
        getAccountUseCase = spyk(
            GetAccountUseCase(accountOutputPort)
        )
    }

    @Test
    fun `should returns an account successfully`() {
        val accountId = UUID.randomUUID()

        val accountMock = AccountDTO(accountId, BigDecimal.ZERO)

        every { accountOutputPort.findById(any<UUID>()) } returns accountMock

        val account = getAccountUseCase.invoke(accountId)

        Assertions.assertNotNull(account)
        Assertions.assertEquals(accountId, account?.id)
        Assertions.assertEquals(accountMock.balance, account?.balance)

        coVerify (exactly = 1) {
            accountOutputPort.findById(any())
        }
    }

    @Test
    fun `should return null when account not exists`() {
        val accountId = UUID.randomUUID()

        every { accountOutputPort.findById(any<UUID>()) } returns null

        val proposal = getAccountUseCase.invoke(accountId)

        Assertions.assertNull(proposal)

        coVerify (exactly = 1) {
            accountOutputPort.findById(any())
        }
    }
}