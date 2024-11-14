package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.output.ProposalAnalysisOutputPort
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.application.utils.AccountRequestMock
import com.mybank.accounts.domain.AnalysisStatus
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class SendProposalUseCaseTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var sendProposalUseCase: SendProposalUseCase

    @MockK
    private lateinit var proposalOutputPort: ProposalOutputPort
    @MockK
    private lateinit var proposalAnalysisOutputPort: ProposalAnalysisOutputPort

    init {
        MockKAnnotations.init(this)
        sendProposalUseCase = spyk(
            SendProposalUseCase(proposalOutputPort, proposalAnalysisOutputPort)
        )
    }

    @Test
    fun `should save proposal and send it for analysis`() {
        val accountRequest = mockAccountRequest.GetAccountRequest()
        val proposalMock = ProposalDTO(UUID.randomUUID(), AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null,accountRequest)

        every { proposalOutputPort.save(any<AccountRequest>()) } returns proposalMock
        every { proposalAnalysisOutputPort.sendToAnalysis(any<ProposalDTO>()) } returns Unit

        val proposal = sendProposalUseCase.invoke(accountRequest)

        Assertions.assertNotNull(proposal)

        coVerify (exactly = 1) {
            proposalOutputPort.save(any())
            proposalAnalysisOutputPort.sendToAnalysis(any())
        }
    }

    @Test
    fun `should return null when proposal save fails`() {
        val accountRequest = mockAccountRequest.GetAccountRequest()
        val proposalMock = ProposalDTO(UUID.randomUUID(), AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null,accountRequest)

        every { proposalOutputPort.save(any<AccountRequest>()) } returns null
        every { proposalAnalysisOutputPort.sendToAnalysis(any<ProposalDTO>()) } returns Unit

        val proposal = sendProposalUseCase.invoke(accountRequest)

        Assertions.assertNull(proposal)

        coVerify (exactly = 1) {
            proposalOutputPort.save(any())
        }

        coVerify (exactly = 0) {
            proposalAnalysisOutputPort.sendToAnalysis(any())
        }
    }
}