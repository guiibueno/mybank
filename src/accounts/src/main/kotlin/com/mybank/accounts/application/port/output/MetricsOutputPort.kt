package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.TransactionResultDTO

interface MetricsOutputPort {
    fun proposalCreated()
    fun proposalHandled(proposal: ProposalDTO)
    fun transactionHandled(transaction: TransactionResultDTO)
    fun accountCreated(account: AccountDTO)
}