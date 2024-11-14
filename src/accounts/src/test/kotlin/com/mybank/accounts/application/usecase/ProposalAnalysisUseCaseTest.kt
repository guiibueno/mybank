package com.mybank.accounts.application.usecase

import com.mybank.accounts.application.dto.AccountDTO
import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.output.ProposalOutputPort
import com.mybank.accounts.application.utils.AccountRequestMock
import com.mybank.accounts.domain.AnalysisStatus
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.junit.jupiter.api.Assertions
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class ProposalAnalysisUseCaseTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var proposalAnalysisUseCase: ProposalAnalysisUseCase

    @MockK
    private lateinit var proposalOutputPort: ProposalOutputPort
    @MockK
    private lateinit var accountRegisterPort: AccountRegisterPort

    init {
        MockKAnnotations.init(this)
        proposalAnalysisUseCase = spyk(
            ProposalAnalysisUseCase(proposalOutputPort, accountRegisterPort)
        )
    }

    @Test
    fun `should approve proposal and return updated ProposalDTO when account is registered`() {
        val proposalId = UUID.randomUUID()
        val accountId = UUID.randomUUID()

        val accountRequest = mockAccountRequest.GetAccountRequest()
        val proposalMock = ProposalDTO(proposalId, AnalysisStatus.IN_ANALYSIS, LocalDateTime.now(), LocalDateTime.now(), null,accountRequest)
        val accountMock = AccountDTO(accountId)

        every { proposalOutputPort.findById(any<UUID>()) } returns proposalMock
        every { proposalOutputPort.update(any<ProposalDTO>()) } returns proposalMock.copy(status = "APPROVED", accountId = accountId)
        every { accountRegisterPort.invoke(any<AccountRequest>()) } returns accountMock

        val proposal = proposalAnalysisUseCase.invoke(proposalId.toString())

        Assertions.assertNotNull(proposal)
        Assertions.assertEquals(proposalId, proposal?.id)
        Assertions.assertEquals(AnalysisStatus.APPROVED, proposal?.status)
        Assertions.assertEquals(accountId, proposal?.accountId)

        coVerify (exactly = 1) {
            proposalOutputPort.findById(any())
            proposalOutputPort.update(any())
            accountRegisterPort.invoke(any())
        }
    }

    @Test
    fun `should return null when proposal is not found`() {
        val proposalId = UUID.randomUUID()

        every { proposalOutputPort.findById(any<UUID>()) } returns null

        val proposal = proposalAnalysisUseCase.invoke(proposalId.toString())

        Assertions.assertNull(proposal)

        coVerify (exactly = 1) {
            proposalOutputPort.findById(any())
        }

        coVerify (exactly = 0) {
            proposalOutputPort.update(any())
            accountRegisterPort.invoke(any())
        }
    }
}