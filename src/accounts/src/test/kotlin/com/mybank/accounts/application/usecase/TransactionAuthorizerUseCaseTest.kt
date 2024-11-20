package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.DistributedLockerRunner
import com.mybank.accounts.application.port.output.MetricsOutputPort
import com.mybank.accounts.application.port.output.TransactionOutputPort
import com.mybank.accounts.domain.exception.DistributedLockException
import com.mybank.accounts.domain.valueobjects.TransactionStatus
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class TransactionAuthorizerUseCaseTest {
    private var transactionAuthorizerUseCase: TransactionAuthorizerUseCase

    @MockK
    private lateinit var distributedLockerRunner: DistributedLockerRunner
    @MockK
    private lateinit var accountOutputPort: AccountOutputPort
    @MockK
    private lateinit var transactionOutputPort: TransactionOutputPort
    @MockK
    private lateinit var metricsOutputPort: MetricsOutputPort

    init {
        MockKAnnotations.init(this)
        transactionAuthorizerUseCase = spyk(
            TransactionAuthorizerUseCase(distributedLockerRunner, accountOutputPort, transactionOutputPort, metricsOutputPort)
        )

        every { metricsOutputPort.transactionHandled(any()) } returns Unit
    }

    @Test
    fun `should returns an APPROVED transaction when successfully processed`() {
        val accountId = UUID.randomUUID()

        val transactionRequest = TransactionRequestDTO(accountId, 'C', BigDecimal.TEN, "Teste")
        val transactionResult = TransactionResultDTO(
            LocalDateTime.now(), TransactionStatus.APPROVED, transactionRequest.type, transactionRequest.amount, BigDecimal.TEN)

        every { accountOutputPort.updateBalance(any<TransactionRequestDTO>()) } returns transactionResult
        every { distributedLockerRunner.tryRunLocked<TransactionResultDTO?>(any(), any(), any()) } returns accountOutputPort.updateBalance(transactionRequest)

        every { transactionOutputPort.emitEvent(any()) } returns Unit

        val transaction = transactionAuthorizerUseCase.invoke(transactionRequest)

        Assertions.assertNotNull(transaction)
        Assertions.assertEquals(transactionRequest.amount, transaction?.amount)
        Assertions.assertEquals(TransactionStatus.APPROVED, transaction?.status)

        coVerify (exactly = 1) {
            distributedLockerRunner.tryRunLocked<TransactionResultDTO?>(any(), any(), any())
            accountOutputPort.updateBalance(any())
            transactionOutputPort.emitEvent(any())
        }
    }

    @Test
    fun `should returns null when not acquire a lock`() {
        val accountId = UUID.randomUUID()

        val transactionRequest = TransactionRequestDTO(accountId, 'C', BigDecimal.TEN, "Teste")

        every { distributedLockerRunner.tryRunLocked<TransactionResultDTO?>(any(), any(), any()) }.throws(DistributedLockException(""))

        val transaction = transactionAuthorizerUseCase.invoke(transactionRequest)

        Assertions.assertNull(transaction)

        coVerify (exactly = 1) {
            distributedLockerRunner.tryRunLocked<TransactionResultDTO?>(any(), any(), any())
        }

        coVerify (exactly = 0) {
            accountOutputPort.updateBalance(any())
            transactionOutputPort.emitEvent(any())
        }
    }
}