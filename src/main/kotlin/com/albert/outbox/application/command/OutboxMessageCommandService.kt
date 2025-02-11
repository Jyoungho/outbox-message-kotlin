package com.albert.outbox.application.command

import com.albert.outbox.config.logeer.logger
import com.albert.outbox.domain.repository.IOutboxMessageRepository
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxMessageCommandService(
    private val outboxMessageRepository: IOutboxMessageRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
) {
    val log = logger()

    @Transactional
    fun sendMessageOutBox(traceId: String) {
        outboxMessageRepository
            .findAllByTraceId(traceId)
            .forEach { message ->
                kafkaTemplate
                    .send(ProducerRecord(message.topic, message.key, message.data))
                    .get(5, java.util.concurrent.TimeUnit.SECONDS)
                log.info("Published message(${message.topic}): $message")

                outboxMessageRepository.delete(message)
            }
    }
}
