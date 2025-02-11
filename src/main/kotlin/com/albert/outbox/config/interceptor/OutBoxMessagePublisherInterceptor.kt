package com.albert.outbox.config.interceptor

import com.albert.outbox.application.command.OutboxMessageCommandService
import com.albert.outbox.config.logeer.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import java.util.*

@Component
class OutBoxMessagePublisherInterceptor(
    private val outboxMessageCommandService: OutboxMessageCommandService,
) : HandlerInterceptor {
    private val log = logger()

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val traceId = UUID.randomUUID().toString()
        MDC.put("omTraceId", traceId)
        return true
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        // 컨트롤러 처리 후 traceId 가져오기
        val traceId = MDC.get("omTraceId")

        outboxMessageCommandService.sendMessageOutBox(traceId)
    }
}
