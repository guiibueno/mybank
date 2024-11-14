package com.mybank.accounts.application.port.output

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest

interface AccountOutputPort {
    fun save(accountRequest: AccountRequest): AccountDTO?
}