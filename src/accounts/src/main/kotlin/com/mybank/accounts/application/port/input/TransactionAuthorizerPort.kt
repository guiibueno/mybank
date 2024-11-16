package com.mybank.accounts.application.port.input

import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO

interface TransactionAuthorizerPort {
    operator fun invoke(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO?
}