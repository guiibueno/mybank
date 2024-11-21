package com.mybank.accounts.domain.entity

import java.math.BigDecimal

data class UpdateBalanceEntity(val success: Boolean,
                               val balance: BigDecimal) {
}