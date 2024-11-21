package com.mybank.accounts.application.dto

import java.math.BigDecimal
import java.util.*

data class AccountDTO(val id: UUID, val balance: BigDecimal) {
}