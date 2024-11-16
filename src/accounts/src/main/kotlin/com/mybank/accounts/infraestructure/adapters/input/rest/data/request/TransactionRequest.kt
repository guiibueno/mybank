package com.mybank.accounts.infraestructure.adapters.input.rest.data.request

import java.math.BigDecimal
import java.util.*

data class TransactionRequest(val type: String,
                              val amount: BigDecimal,
                              val description: String) {

}