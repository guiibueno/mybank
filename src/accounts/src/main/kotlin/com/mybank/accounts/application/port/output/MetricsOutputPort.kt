package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.TransactionResultDTO

interface MetricsOutputPort {
    fun transactionHandled(transaction: TransactionResultDTO)
    fun accountRegisterRequested()
    fun accountCreated(account: AccountDTO)
}