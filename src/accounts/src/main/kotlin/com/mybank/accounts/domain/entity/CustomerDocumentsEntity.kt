package com.mybank.accounts.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("customerdocuments")
data class CustomerDocumentsEntity(@Id val id: String?,
                                   val createdat: LocalDateTime,
                                   val customerid: String,
                                   val type: String,
                                   val number: String) {

    constructor(customerid: String, type: String, number: String) : this (null, LocalDateTime.now(), customerid, type, number) { }
}