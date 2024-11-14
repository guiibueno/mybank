package com.mybank.accounts.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("accounts")
data class AccountEntity(@Id val id: String?,
                         val active: Boolean,
                         val createdat: LocalDateTime,
                         val customerid: String,
                         val balance: BigDecimal) {
}