package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.input.TransactionAuthorizerPort
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.application.port.output.MetricsOutputPort
import com.mybank.accounts.application.port.output.TransactionOutputPort
import com.mybank.accounts.domain.event.TransactionEvent
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class TransactionAuthorizerUseCase(
    val redissonClient: RedissonClient,
    val accountOutputPort: AccountOutputPort,
    val transactionOutputPort: TransactionOutputPort,
    val metricsOutputPort: MetricsOutputPort
) : TransactionAuthorizerPort {
    override fun invoke(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO? {
        val id = UUID.randomUUID()
        var transactionResult: TransactionResultDTO? = null
        val accountLock = redissonClient.getLock("lock-account-${transactionRequestDTO.accountId}")

        if(accountLock.tryLock(30, 15, TimeUnit.SECONDS)){
            try{
                transactionResult = accountOutputPort.updateBalance(transactionRequestDTO)
            }
            finally {
                accountLock.unlock()
            }
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