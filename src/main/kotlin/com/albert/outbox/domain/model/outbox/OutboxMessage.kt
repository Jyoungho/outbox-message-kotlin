package com.albert.outbox.domain.model.outbox

import jakarta.persistence.*
import org.springframework.data.domain.AbstractAggregateRoot
import java.time.LocalDateTime

@Entity
@Table(
    name = "outbox_message",
    indexes = [
        Index(name = "idx_outbox_message_trace_id", columnList = "trace_id"),
    ],
)
class OutboxMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(name = "type", nullable = true)
    val type: String? = null,
    @Column(name = "topic")
    val topic: String,
    @Column(name = "key")
    val key: String,
    @Column(name = "data", columnDefinition = "text")
    val data: String,
    @Column(name = "trace_id")
    val traceId: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : AbstractAggregateRoot<OutboxMessage>() {
    override fun toString(): String =
        "OutboxMessage(id=$id, type=$type, topic='$topic', key='$key', data='$data', traceId='$traceId', createdAt=$createdAt)"
}
