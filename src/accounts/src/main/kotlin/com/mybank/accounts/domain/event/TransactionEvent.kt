package com.mybank.accounts.domain.event

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class TransactionEvent (val authorizationId: UUID,
                             val timestamp: LocalDateTime,
                             val status: String,
                             val type: Char,
                             val amount: BigDecimal) {
}