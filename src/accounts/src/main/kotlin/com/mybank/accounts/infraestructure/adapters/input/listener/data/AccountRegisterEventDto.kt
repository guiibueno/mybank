package com.mybank.accounts.infraestructure.adapters.input.listener.data

import com.mybank.accounts.application.dto.AccountRequest
import java.util.UUID

data class AccountRegisterEventDto(
    val proposalId: UUID,
    val accountRequest: AccountRequest
) {
}