package com.mybank.accounts.infraestructure.adapters.output.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.output.CacheOutputPort
import com.mybank.accounts.domain.AnalysisStatus
import com.mybank.accounts.domain.entity.ProposalEntity
import com.mybank.accounts.infraestructure.adapters.output.persistence.repository.ProposalRepository
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class ProposalPersistenceTest {
    private var mockAccountRequest = AccountRequestMock()

    private var proposalPersistence: ProposalPersistence

    private var mapper: ObjectMapper

    @MockK
    private lateinit var cache: CacheOutputPort
    @MockK
    private lateinit var proposalRepository: ProposalRepository

    init {
        MockKAnnotations.init(this)

        mapper = ObjectMapper()
        mapper.findAndRegisterModules()

        proposalPersistence = spyk(
            ProposalPersistence(cache, mapper, proposalRepository)
        )
    }

    @Test
    fun `should return data when proposal is created`() {
        val request = mockAccountRequest.GetAccountRequest()
        val requestJsonString = mapper.writeValueAsString(request)
        val response = ProposalDTO(UUID.randomUUID(), AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null, request)

        every { proposalRepository.save(any()) } returns ProposalEntity(response.id.toString(), response.status, response.createdAt, response.updatedAt, response.accountId?.toString(), requestJsonString)
        every { cache.setValue(any(), any(), any()) } returns Unit

        val proposal = proposalPersistence.save(request)

        Assertions.assertNotNull(proposal)
        Assertions.assertNotNull(proposal?.id)
        Assertions.assertEquals(request.name, proposal?.additionalinfos?.name)

        coVerify (exactly = 1) {
            proposalRepository.save(any())
        }
    }

    @Test
    fun `should return null when proposal not exists`() {
        val proposalId = UUID.randomUUID()

        every { proposalRepository.findById(any()) } returns Optional.empty()
        every { cache.getValue(any(), ProposalDTO::class.java) } returns null

        val proposal = proposalPersistence.findById(proposalId)

        Assertions.assertNull(proposal)

        coVerify (exactly = 1) {
            proposalRepository.findById(any())
            cache.getValue(any(), ProposalDTO::class.java)
        }
    }

    @Test
    fun `should return data from repository when proposal exists but is not cached`() {
        val proposalId = UUID.randomUUID()
        val request = mockAccountRequest.GetAccountRequest()
        val requestJsonString = mapper.writeValueAsString(request)
        val entity = ProposalEntity(proposalId.toString(), AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID().toString(), requestJsonString)

        every { proposalRepository.findById(any()) } returns Optional.of(entity)
        every { cache.getValue(any(), ProposalDTO::class.java) } returns null
        every { cache.setValue(any(), any(), any()) } returns Unit

        val proposal = proposalPersistence.findById(proposalId)

        Assertions.assertNotNull(proposal)
        Assertions.assertNotNull(proposal?.id)

        coVerify (exactly = 1) {
            proposalRepository.findById(any())
            cache.getValue(any(), ProposalDTO::class.java)
        }
    }

    @Test
    fun `should return data from cache when proposal exists`() {
        val proposalId = UUID.randomUUID()
        val request = mockAccountRequest.GetAccountRequest()
        val requestJsonString = mapper.writeValueAsString(request)
        val entity = ProposalEntity(proposalId.toString(), AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID().toString(), requestJsonString)

        val dto = ProposalDTO(UUID.fromString(entity.id), entity.status, entity.createdat, entity.updatedat, UUID.fromString(entity.accountid), request)

        every { proposalRepository.findById(any()) } returns Optional.of(entity)
        every { cache.getValue(any(), ProposalDTO::class.java) } returns dto

        val proposal = proposalPersistence.findById(proposalId)

        Assertions.assertNotNull(proposal)
        Assertions.assertNotNull(proposal?.id)

        coVerify (exactly = 1) {
            cache.getValue(any(), ProposalDTO::class.java)
        }

        coVerify (exactly = 0) {
            proposalRepository.findById(any())
        }
    }
}