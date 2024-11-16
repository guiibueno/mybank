package com.mybank.accounts.infraestructure.adapters.output.event

import com.mybank.accounts.application.port.output.TransactionOutputPort
import com.mybank.accounts.domain.event.TransactionEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class TransactionProducer(
    @Value("\${topics.transactions.name}")
    private val transactionsTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, TransactionEvent>
) : TransactionOutputPort {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun emitEvent(event: TransactionEvent) {
        kafkaTemplate.send(transactionsTopic, event)

        logger.info("[Transaction ${event.authorizationId}] - New transaction submitted.")
    }
}