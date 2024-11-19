package com.mybank.accounts.infraestructure.adapters.output.event

import com.mybank.accounts.application.port.output.AccountCreatedOutputPort
import com.mybank.accounts.domain.event.AccountCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class AccountCreatedProducer(
    @Value("\${topics.accounts.registers.name}")
    private val newAccountsTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, AccountCreatedEvent>
) : AccountCreatedOutputPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun emitEvent(event: AccountCreatedEvent) {
        kafkaTemplate.send(newAccountsTopic, event)

        logger.info("[Account ${event.accountId}] - New account submitted.")
    }
}