package com.mybank.accounts.infraestructure.adapters.output.persistence.repository

import com.mybank.accounts.domain.entity.AccountEntity
import com.mybank.accounts.domain.entity.UpdateBalanceEntity
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.math.BigDecimal

interface AccountRepository : CrudRepository<AccountEntity, String> {

    @Query(value = "select * from update_balance(:accountid, :transactiontype, :transactionvalue);")
    fun updateBalance(@Param("accountid") accountId: String,
                      @Param("transactiontype")transactionType: Char,
                      @Param("transactionvalue")transactionValue: BigDecimal): UpdateBalanceEntity
}