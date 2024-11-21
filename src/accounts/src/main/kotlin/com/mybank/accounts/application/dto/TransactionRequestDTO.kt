package com.mybank.accounts.application.dto

import java.math.BigDecimal
import java.util.*

data class TransactionRequestDTO(val accountId: UUID,
                                 val type: Char,
                                 val amount: BigDecimal,
                                 val description: String) {

    init {
        validate()
    }

    private fun validate() {
        require(isValidType)
        require(isValidAmount)
        require(isValidDescription)
    }

    private val isValidType: Boolean
        get() = type == 'C' || type == 'D'

    private val isValidAmount: Boolean
        get() = amount > BigDecimal.ZERO

    private val isValidDescription: Boolean
        get() = description != "" && description.length <= 50


}