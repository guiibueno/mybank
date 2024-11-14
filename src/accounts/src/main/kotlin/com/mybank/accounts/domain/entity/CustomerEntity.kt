package com.mybank.accounts.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("customers")
data class CustomerEntity(@Id val id: String?,
                          val createdat: LocalDateTime,
                          val name: String,
                          val birthdate: LocalDate
) {
}