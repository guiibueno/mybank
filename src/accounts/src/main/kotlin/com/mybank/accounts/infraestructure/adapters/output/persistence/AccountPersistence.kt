package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.AccountOutputPort
import com.mybank.accounts.domain.entity.AccountEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*


@Service
class AccountPersistence (
    val customerPersistence: CustomerPersistence,
    val documentsPersistence: DocumentsPersistence,
    val accountRepository: AccountRepository) : AccountOutputPort {
    override fun save(accountRequest: AccountRequest): AccountDTO? {
        val customer = customerPersistence.save(accountRequest.name, accountRequest.birthDate)

        if(!customer.id.isNullOrBlank()){
            documentsPersistence.save(customer.id, accountRequest.documents)

            val accountEntity = AccountEntity(null, true, LocalDateTime.now(), customer.id, BigDecimal.ZERO)

            val entity = accountRepository.save(accountEntity)

            return AccountDTO(UUID.fromString(entity.id), entity.balance)
        }

        return null
    }

    override fun findById(id: UUID): AccountDTO? {
        val result = accountRepository.findById(id.toString());

        if(result.isEmpty)
            return null

        val entity = result.get()

        return AccountDTO(UUID.fromString(entity.id), entity.balance)
    }
}