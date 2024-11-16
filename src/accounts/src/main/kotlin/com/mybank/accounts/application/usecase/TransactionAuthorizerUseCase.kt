package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.input.TransactionAuthorizerPort
import com.mybank.accounts.application.port.output.AccountOutputPort
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
    val transactionOutputPort: TransactionOutputPort
) : TransactionAuthorizerPort {
    override fun invoke(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO? {
        val id = UUID.randomUUID()
        val accountLock = redissonClient.getLock("lock-account-${transactionRequestDTO.accountId}")

        if(accountLock.tryLock(30, 15, TimeUnit.SECONDS)){
            try{
                val result = accountOutputPort.updateBalance(transactionRequestDTO.accountId, transactionRequestDTO)
                val event = TransactionEvent(id, result.timestamp, result.status, result.type, result.amount)
                transactionOutputPort.emitEvent(event)

                return result;
            }
            finally {
                accountLock.unlock()
            }
        }

        return null
    }
}