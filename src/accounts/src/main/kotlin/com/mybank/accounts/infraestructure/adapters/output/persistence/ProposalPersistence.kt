package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.domain.AnalysisStatus
import com.mybank.accounts.domain.entity.ProposalEntity
import com.mybank.accounts.application.port.output.CacheAdapter
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.ProposalRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class ProposalPersistence(
    val cache: CacheAdapter,
    val mapper: ObjectMapper,
    val proposalRepository: ProposalRepository) : ProposalOutputPort {

    private fun getCacheKey(id: UUID): String = "Accounts.:${id.toString()}"

    override fun save(proposal: AccountRequest): ProposalDTO? {
        val proposalDetails: String = mapper.writeValueAsString(proposal);
        val proposalEntity = ProposalEntity(null, AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null, proposalDetails)

        val entity = proposalRepository.save(proposalEntity)

        val dto = ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, null, proposal)
        cache.setValue(getCacheKey(dto.id), dto, 30)

        return dto

    }

    override fun findById(id: UUID): ProposalDTO? {
        val cachedValue = cache.getValue(getCacheKey(id), ProposalDTO::class.java)
        if(cachedValue != null)
            return cachedValue

        val result = proposalRepository.findById(id.toString());

        if(result.isEmpty)
            return null

        val entity = result.get()

        val accountId = if (entity.accountid == null) null else UUID.fromString(entity.accountid)

        val proposalDetails: AccountRequest = mapper.readValue(entity.additionalinfos, AccountRequest::class.java)

        val dto = ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, accountId, proposalDetails)
        cache.setValue(getCacheKey(dto.id), dto, 30)

        return dto;
    }

    override fun update(proposal: ProposalDTO): ProposalDTO {
        val proposalDetails: String = mapper.writeValueAsString(proposal.additionalinfos);
        val proposalEntity = ProposalEntity(proposal.id.toString(), proposal.status, proposal.createdAt, LocalDateTime.now(), proposal.accountId.toString(), proposalDetails)

        val entity = proposalRepository.save(proposalEntity)


        val dto = ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, UUID.fromString(entity.accountid), proposal.additionalinfos)
        cache.setValue(getCacheKey(dto.id), dto, 30)

        return dto;
    }
}