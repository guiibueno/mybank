package com.mybank.accounts.infraestructure.adapters.output.event

import com.mybank.accounts.application.dto.AccountRequest
import com.mybank.accounts.application.port.output.AccountRegisterOutputPort
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class AccountRegisterProducer(
    @Value("\${topics.accounts.requests.name}")
    private val newAccountsTopic: String,
    private val kafkaTemplate: KafkaTemplate<String, AccountRequest>
) : AccountRegisterOutputPort {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendToAsyncProcess(accountRequest: AccountRequest) {
        kafkaTemplate.send(newAccountsTopic, accountRequest)

        logger.info("New account register requested.")
    }

}