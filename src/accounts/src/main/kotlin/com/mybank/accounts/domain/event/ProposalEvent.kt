package com.mybank.accounts.domain.event

import java.time.LocalDateTime

data class ProposalEvent(val id: String,
                         val createdAt: LocalDateTime) {
}