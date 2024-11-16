package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.utils.AccountRequestMock
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class GetProposalStatusUseCaseTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var getProposalStatusUseCase: GetProposalStatusUseCase

    @MockK
    private lateinit var proposalOutputPort: ProposalOutputPort

    init {
        MockKAnnotations.init(this)
        getProposalStatusUseCase = spyk(
            GetProposalStatusUseCase(proposalOutputPort)
        )
    }

    @Test
    fun `should returns an proposal successfully`() {
        val proposalId = UUID.randomUUID();
        val proposalMock = ProposalDTO(proposalId, "APPROVED", LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID(), mockAccountRequest.GetAccountRequest())

        every { proposalOutputPort.findById(any<UUID>()) } returns proposalMock

        val proposal = getProposalStatusUseCase.invoke(proposalId)

        Assertions.assertNotNull(proposal)
        Assertions.assertEquals(proposalId, proposal?.id)

        coVerify (exactly = 1) {
            proposalOutputPort.findById(any())
        }
    }

    @Test
    fun `should returns null when proposal not exists`() {
        val proposalId = UUID.randomUUID();
        val proposalMock = ProposalDTO(proposalId, "APPROVED", LocalDateTime.now(), LocalDateTime.now(), UUID.randomUUID(), mockAccountRequest.GetAccountRequest())

        every { proposalOutputPort.findById(any<UUID>()) } returns null

        val proposal = getProposalStatusUseCase.invoke(proposalId)

        Assertions.assertNull(proposal)

        coVerify (exactly = 1) {
            proposalOutputPort.findById(any())
        }
    }
}