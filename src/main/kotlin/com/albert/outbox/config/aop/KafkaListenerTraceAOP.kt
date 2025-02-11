package com.albert.outbox.config.aop

import com.albert.outbox.application.command.OutboxMessageCommandService
import com.albert.outbox.config.logeer.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.*

@Component
@Aspect
class KafkaListenerTraceAOP(
    private val outboxMessageCommandService: OutboxMessageCommandService,
) {
    val log = logger()

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    fun apiRestPointcut() {
    }

    @Around("apiRestPointcut()")
    fun trace(joinPoint: ProceedingJoinPoint): Any {
        // record 정보 가져오기
        val traceId = UUID.randomUUID().toString()
        MDC.put("traceId", traceId)

        try {
            joinPoint.proceed()
        } catch (e: Exception) {
            log.error(
                """
                kafka listener error traceId: $traceId 
                message: ${e.message}
                stackTrace: ${e.stackTrace.joinToString("\n")}            
                """.trimIndent()
            )
            throw e
        }

        outboxMessageCommandService.sendMessageOutBox(traceId)

        return true
    }
}
