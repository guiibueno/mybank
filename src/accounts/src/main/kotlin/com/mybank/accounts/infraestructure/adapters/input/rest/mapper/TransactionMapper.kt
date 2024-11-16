package com.mybank.accounts.infraestructure.adapters.input.rest.mapper

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.infraestructure.adapters.input.rest.data.request.TransactionRequest
import java.util.*

class TransactionMapper {
    fun convertToPortInput(accountId: UUID, request: TransactionRequest) : TransactionRequestDTO{
        return TransactionRequestDTO(accountId, normalizeTransactionType(request.type), request.amount, request.description)
    }

    private fun normalizeTransactionType(type: String): Char = type.uppercase()[0]
}