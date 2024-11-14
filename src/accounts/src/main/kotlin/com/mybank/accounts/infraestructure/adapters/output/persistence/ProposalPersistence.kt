package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.domain.AnalysisStatus
import com.mybank.accounts.domain.entity.ProposalEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.ProposalRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProposalPersistence(val proposalRepository: ProposalRepository) : ProposalOutputPort {
    val mapper = ObjectMapper()

    init{
        mapper.findAndRegisterModules()
    }

    override fun save(proposal: AccountRequest): ProposalDTO? {
        val proposalDetails: String = mapper.writeValueAsString(proposal);
        val proposalEntity = ProposalEntity(null, AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null, proposalDetails)

        val entity = proposalRepository.save(proposalEntity)

        return ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, null, proposal)
    }

    override fun findById(id: UUID): ProposalDTO? {
        val result = proposalRepository.findById(id.toString());

        if(result.isEmpty)
            return null

        val entity = result.get()

        val accountId = if (entity.accountid == null) null else UUID.fromString(entity.accountid)

        val proposalDetails: AccountRequest = mapper.readValue(entity.additionalinfos, AccountRequest::class.java)

        return ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, accountId, proposalDetails)
    }

    override fun update(proposal: ProposalDTO): ProposalDTO {
        val proposalDetails: String = mapper.writeValueAsString(proposal.additionalinfos);
        val proposalEntity = ProposalEntity(proposal.id.toString(), proposal.status, proposal.createdAt, LocalDateTime.now(), proposal.accountId.toString(), proposalDetails)

        val entity = proposalRepository.save(proposalEntity)

        return ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, UUID.fromString(entity.accountid), proposal.additionalinfos)
    }
}