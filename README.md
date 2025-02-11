# outbox-message-kotlin
Transactional Outbox Message (Kotlin)

## 🔭 What
**Transactional Outbox Pattern** 은 마이크로서비스 아키텍처에서 데이터베이스의 상태 변경과 이벤트 발행의 원자성을 보장하기 위한 설계 패턴입니다. 이는 데이터베이스 업데이트와 이벤트 발행을 하나의 트랜잭션으로 처리하여 데이터 일관성을 유지하는 데 도움을 줍니다.

[Microservices Pattern: Transactional Outbox](https://microservices.io/patterns/data/transactional-outbox.html)

**장점:**
- **데이터 일관성 유지:** 데이터베이스 업데이트와 이벤트 발행을 하나의 트랜잭션으로 처리하여 데이터 일관성을 보장합니다.
- **신뢰성 향상:** 네트워크 오류나 시스템 장애 시에도 Outbox 테이블에 저장된 이벤트를 통해 재시도가 가능하여 신뢰성을 높입니다.
- **확장성:** 이벤트 발행 로직을 별도의 프로세스로 분리하여 시스템의 확장성과 유지 보수성을 향상시킵니다.
  
**단점:**
- **복잡성 증가:** Outbox 테이블과 이벤트 발행 프로세스를 추가로 관리해야 하므로 시스템의 복잡성이 증가할 수 있습니다.
- **지연 가능성:** 이벤트 발행이 비동기로 처리되므로, 실시간 처리가 필요한 경우 지연이 발생할 수 있습니다.

## 🤔 How

**RESTful API**

RESTful API에서는 하나의 트랜잭션으로 묶어야 하는 작업들을 처리하기 위해 Interceptor를 활용하여 omTraceId(OutboxMessage TraceId)를 생성한다. 이를 통해 트랜잭션이 완전히 종료된 후 안정적으로 OutboxMessage 를 발행할 수 있다. 

**Kafka Message**

Spring Kafka를 사용할 경우, KafkaListener를 통해 메시지를 소비한다. 이때, **AOP(Aspect-Oriented Programming)**를 적용하여 하나의 omTraceId로 묶어주는 설정을 구현함으로써 일관된 OutboxMessage 흐름을 유지할 수 있도록 한다.

## HOW TO USE

**Kafka Message 발행 대신, OutboxMessage 로 저장**

```kotlin
@Service
@Transactional(propagation = Propagation.REQUIRED)
class KafkaProducer(
    private val objectMapper: ObjectMapper,
    private val outboxMessageRepository: IOutboxMessageRepository,
) : IKafkaProducer {
    var log = logger()
    
    // ...

    override fun send(
        topic: String,
        key: String,
        message: Any,
    ) {
        var transactionName: String? = null
        val jsonData = objectMapper.writeValueAsString(message)

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            transactionName = TransactionSynchronizationManager.getCurrentTransactionName()
        }

        val traceId = MDC.get("omTraceId")

        val outboxMessage =
            OutboxMessage(
                type = transactionName,
                topic = topic,
                key = key,
                data = jsonData,
                traceId = traceId,
            )
        outboxMessageRepository.save(outboxMessage)
    }
}

```

**발행처리**

outboxMessage 를 정상적으로 저장하였다면, outbox library 에서 해당 메시지를 발행한다.



