package com.mybank.accounts.domain.entity

import com.mybank.accounts.application.dto.SendProposalRequest
import org.springframework.data.annotation.Id
import org.springframework.data.convert.ValueConverter
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("proposals")
data class ProposalEntity(@Id
                          val id: String?,
                          val status: String,
                          val createdat: LocalDateTime,
                          val updatedat: LocalDateTime,
                          val accountid: String?,
                          //val additionalinfos: SendProposalRequest
) {


}