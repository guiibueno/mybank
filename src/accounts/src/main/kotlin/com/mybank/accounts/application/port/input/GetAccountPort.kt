package com.mybank.accounts.application.port.input

import com.mybank.accounts.application.dto.AccountDTO
import java.util.*

interface GetAccountPort {
    operator fun invoke(accountId: UUID): AccountDTO?
    operator fun invoke(documentNumber: String): AccountDTO?
}