package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.MetricsOutputPort
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

class AccountRegisterUseCaseTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var accountRegisterUseCase: AccountRegisterUseCase

    @MockK
    private lateinit var accountOutputPort: AccountOutputPort
    @MockK
    private lateinit var metricsOutputPort: MetricsOutputPort

    init {
        MockKAnnotations.init(this)
        accountRegisterUseCase = spyk(
            AccountRegisterUseCase(accountOutputPort, metricsOutputPort)
        )

        every { metricsOutputPort.accountCreated(any()) } returns Unit
    }

    @Test
    fun `should returns an account when successfully created`() {
        val accountId = UUID.randomUUID()

        val accountRequest = mockAccountRequest.GetAccountRequest()

        every { accountOutputPort.save(any<AccountRequest>()) } returns AccountDTO(accountId, BigDecimal.ZERO)

        val account = accountRegisterUseCase.invoke(accountRequest)

        Assertions.assertNotNull(account)
        Assertions.assertEquals(accountId, account?.id)

        coVerify (exactly = 1) {
            accountOutputPort.save(any())
        }
    }

    @Test
    fun `should returns null when creation failed`() {
        val accountRequest = mockAccountRequest.GetAccountRequest()

        every { accountOutputPort.save(any<AccountRequest>()) } returns null

        val account = accountRegisterUseCase.invoke(accountRequest)

        Assertions.assertNull(account)

        coVerify (exactly = 1) {
            accountOutputPort.save(any())
        }
    }
}