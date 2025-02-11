package com.albert.outbox.domain.repository

import com.albert.outbox.domain.model.outbox.OutboxMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface IOutboxMessageRepository : JpaRepository<OutboxMessage, Long> {
    fun findAllByTraceId(traceId: String): List<OutboxMessage>

    @Query("SELECT o FROM OutboxMessage o WHERE o.createdAt < :tenMinutesAgo")
    fun findAllAbnormal(
        @Param("tenMinutesAgo") tenMinutesAgo: LocalDateTime = LocalDateTime.now().minusMinutes(10),
    ): List<OutboxMessage>
}
