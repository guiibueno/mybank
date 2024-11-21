package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.TransactionRequestDTO
import com.mybank.accounts.application.dto.TransactionResultDTO
import java.util.*

interface AccountOutputPort {
    fun save(accountRequest: AccountRequest): AccountDTO?
    fun findById(id: UUID): AccountDTO?
    fun updateBalance(transactionRequestDTO: TransactionRequestDTO): TransactionResultDTO?
    fun findByDocument(documentType: String, documentNumber: String): List<AccountDTO>
}