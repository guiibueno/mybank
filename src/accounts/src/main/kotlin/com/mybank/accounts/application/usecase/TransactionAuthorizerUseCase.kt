package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.input.TransactionAuthorizerPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.DistributedLockerRunner
import com.mybank.accounts.application.port.output.MetricsOutputPort
import com.mybank.accounts.application.port.output.TransactionOutputPort
import com.mybank.accounts.domain.event.TransactionEvent
import com.mybank.accounts.domain.exception.DistributedLockException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class TransactionAuthorizerUseCase(
    val distributedLock: DistributedLockerRunner,
    val accountOutputPort: AccountOutputPort,
    val transactionOutputPort: TransactionOutputPort,
    val metricsOutputPort: MetricsOutputPort
) : TransactionAuthorizerPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun invoke(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO? {
        val id = UUID.randomUUID()
        var transactionResult: TransactionResultDTO? = null

        try{
            transactionResult = distributedLock.tryRunLocked<TransactionResultDTO?>(key = "lock-account-${transactionRequestDTO.accountId}", optionalLock = false) {
                accountOutputPort.updateBalance(transactionRequestDTO)
            }
        } catch (lockException: DistributedLockException){
            logger.error("Error to lock account for transaction", lockException)
        }

        if(transactionResult != null){
            sendEvent(id, transactionResult)
            metricsOutputPort.transactionHandled(transactionResult)
        }

        return transactionResult
    }

    private fun sendEvent(id: UUID, result: TransactionResultDTO){
        val event = TransactionEvent(id, result.timestamp, result.status, result.type, result.amount)
        transactionOutputPort.emitEvent(event)
    }
}