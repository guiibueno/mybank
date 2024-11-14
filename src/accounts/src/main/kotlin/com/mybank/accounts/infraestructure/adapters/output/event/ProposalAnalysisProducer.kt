package com.mybank.accounts.infraestructure.adapters.output.event

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.port.output.ProposalAnalysisOutputPort
import com.mybank.accounts.domain.event.ProposalEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProposalAnalysisProducer(
    @Value("\${topics.proposals.name}")
    private val newProposalTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, ProposalEvent>
) : ProposalAnalysisOutputPort {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendToAnalysis(proposal: ProposalDTO) {
        val event = ProposalEvent(proposal.id.toString(), proposal.createdAt)
        kafkaTemplate.send(newProposalTopic, event)

        logger.info("[Proposal ${proposal.id}] - Submitted to analysis.")
    }
}