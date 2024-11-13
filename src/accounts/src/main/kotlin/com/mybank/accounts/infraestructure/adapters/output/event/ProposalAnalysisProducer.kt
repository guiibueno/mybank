package com.mybank.accounts.infraestructure.adapters.output.event

import com.mybank.accounts.application.dto.ProposalDTO
import com.mybank.accounts.application.dto.SendProposalRequest
import com.mybank.accounts.application.port.output.ProposalAnalysisOutputPort
import com.mybank.accounts.domain.entity.ProposalEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class ProposalAnalysisProducer(
    @Value("\${topics.proposals.name}")
    private val newProposalTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, ProposalDTO>
) : ProposalAnalysisOutputPort {

    override fun sendToAnalysis(proposal: ProposalDTO) {
        kafkaTemplate.send(newProposalTopic, proposal)
    }
}