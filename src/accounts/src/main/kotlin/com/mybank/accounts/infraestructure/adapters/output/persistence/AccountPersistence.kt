package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.domain.entity.AccountEntity
import com.mybank.accounts.domain.valueobjects.TransactionStatus
import com.mybank.accounts.application.port.output.CacheOutputPort
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@Service
class AccountPersistence (
    val cache: CacheOutputPort,
    val customerPersistence: CustomerPersistence,
    val documentsPersistence: DocumentsPersistence,
    val accountRepository: AccountRepository) : AccountOutputPort
{
    private fun getCacheKey(id: UUID): String = "Accounts.:${id.toString()}"

    override fun save(accountRequest: AccountRequest): AccountDTO? {
        val customer = customerPersistence.save(accountRequest.name, accountRequest.birthDate)

        if(!customer.id.isNullOrBlank()){
            documentsPersistence.save(customer.id, accountRequest.documents)

            val accountEntity = AccountEntity(null, true, LocalDateTime.now(), customer.id, BigDecimal.ZERO)

            val entity = accountRepository.save(accountEntity)
            val dto = AccountDTO(UUID.fromString(entity.id), entity.balance)
            cache.setValue(getCacheKey(dto.id), dto, 30)

            return dto
        }

        return null
    }

    override fun findById(id: UUID): AccountDTO? {
        val cachedValue = cache.getValue(getCacheKey(id), AccountDTO::class.java)
        if(cachedValue == null){
            val result = accountRepository.findById(id.toString());

            if(result.isEmpty)
                return null

            val entity = result.get()
            val dto = AccountDTO(UUID.fromString(entity.id), entity.balance)
            cache.setValue(getCacheKey(dto.id), dto, 30)

            return dto;
        }

        return cachedValue
    }

    override fun updateBalance(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO? {
        val result = accountRepository.updateBalance(transactionRequestDTO.accountId.toString(), transactionRequestDTO.type, transactionRequestDTO.amount)

        if(result != null) {
           val transactionStatus: String = if(result.success) TransactionStatus.APPROVED else TransactionStatus.REJECTED
            cache.expire(getCacheKey(transactionRequestDTO.accountId))

            return TransactionResultDTO(LocalDateTime.now(), transactionStatus, transactionRequestDTO.type, transactionRequestDTO.amount, result.balance)
        }

        return null
    }
}