package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.AccountCreatedOutputPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.AccountRegisterOutputPort
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
    private lateinit var accountRegisterOutputPort: AccountRegisterOutputPort
    @MockK
    private lateinit var accountCreatedOutputPort: AccountCreatedOutputPort
    @MockK
    private lateinit var metricsOutputPort: MetricsOutputPort

    init {
        MockKAnnotations.init(this)
        accountRegisterUseCase = spyk(
            AccountRegisterUseCase(accountOutputPort, accountRegisterOutputPort, accountCreatedOutputPort, metricsOutputPort)
        )

        every { accountCreatedOutputPort.emitEvent(any()) } returns Unit
        every { metricsOutputPort.accountCreated(any()) } returns Unit
        every { metricsOutputPort.accountRegisterRequested() } returns Unit
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
            accountCreatedOutputPort.emitEvent(any())
            metricsOutputPort.accountCreated(any())
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

        coVerify (exactly = 0) {
            accountCreatedOutputPort.emitEvent(any())
            metricsOutputPort.accountCreated(any())
        }
    }

    @Test
    fun `should send to async process successfully`() {
        val accountRequest = mockAccountRequest.GetAccountRequest()

        every { accountRegisterOutputPort.sendToAsyncProcess(any()) } returns Unit

        accountRegisterUseCase.invokeAsync(accountRequest)

        coVerify (exactly = 1) {
            accountRegisterOutputPort.sendToAsyncProcess(any())
            metricsOutputPort.accountRegisterRequested()
        }

        coVerify (exactly = 0) {
            accountOutputPort.save(any())
        }
    }
}