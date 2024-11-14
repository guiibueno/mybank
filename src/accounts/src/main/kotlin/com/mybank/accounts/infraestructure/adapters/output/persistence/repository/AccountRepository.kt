package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.AccountEntity
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<AccountEntity, String> {
}