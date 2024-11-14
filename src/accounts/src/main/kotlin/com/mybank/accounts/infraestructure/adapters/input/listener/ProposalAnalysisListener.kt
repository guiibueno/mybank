package com.mybank.accounts.infraestructure.adapters.input.listener

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.input.AccountRegisterPort
import com.mybank.accounts.application.port.input.ProposalAnalysisPort
import com.mybank.accounts.domain.event.ProposalEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ProposalAnalysisListener(
    private val proposalAnalysisPort: ProposalAnalysisPort
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @KafkaListener(
        id = "\${topics.proposals.name}",
        topics = ["\${topics.proposals.name}"])
    fun handle(record: ConsumerRecord<String, ProposalEvent> ){
        logger.info("[Proposal ${record.value().id}] - Initializing analysis.")

        val result = proposalAnalysisPort.invoke(record.value().id)

        if(result == null) {
            logger.info("[Proposal ${record.value().id}] - Not found.")
            return
        }

        logger.info("[Proposal ${record.value().id}] - Status: ${result.status} | AccountId ${result.accountId}.")
    }
}