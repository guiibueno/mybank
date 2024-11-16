package com.mybank.accounts.infraestructure.adapters.input.listener

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.input.ProposalAnalysisPort
import com.mybank.accounts.utils.AccountRequestMock
import com.mybank.accounts.domain.event.ProposalEvent
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

class ProposalAnalysisListenerTest {
    private var mockAccountRequest: AccountRequestMock = AccountRequestMock()

    private var proposalAnalysisListener: ProposalAnalysisListener

    @MockK
    private lateinit var proposalAnalysisPort: ProposalAnalysisPort

    init {
        MockKAnnotations.init(this)
        proposalAnalysisListener = spyk(
            ProposalAnalysisListener(proposalAnalysisPort)
        )
    }

    @Test
    fun `should process analysis when event is received`() {
        val proposalId = UUID.randomUUID()
        val event = ProposalEvent(proposalId.toString(), LocalDateTime.now())
        val kafkaMessage = ConsumerRecord<String, ProposalEvent>("topic", 0, 123L, "key", event)

        every { proposalAnalysisPort.invoke(any()) } returns ProposalDTO(proposalId, "APPROVED", event.createdAt, LocalDateTime.now(), UUID.randomUUID(), mockAccountRequest.GetAccountRequest())

        proposalAnalysisListener.handle(kafkaMessage)

        coVerify (exactly = 1) {
            proposalAnalysisPort.invoke(any())
        }
    }
}