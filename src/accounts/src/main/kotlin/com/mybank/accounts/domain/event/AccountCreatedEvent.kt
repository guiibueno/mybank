package com.mybank.accounts.domain.event;

import java.util.UUID;

data class AccountCreatedEvent(val accountId: UUID) {
}