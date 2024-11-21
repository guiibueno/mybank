package com.mybank.accounts.application.port.input

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest

interface AccountRegisterPort {
    fun invoke(accountRequest: AccountRequest): AccountDTO?
    fun invokeAsync(accountRequest: AccountRequest)
}