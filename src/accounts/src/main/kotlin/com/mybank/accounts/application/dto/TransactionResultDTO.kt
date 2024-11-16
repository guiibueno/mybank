package com.mybank.accounts.application.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionResultDTO(val timestamp: LocalDateTime,
                                val status: String,
                                val type: Char,
                                val amount: BigDecimal,
                                val balance: BigDecimal) {
}