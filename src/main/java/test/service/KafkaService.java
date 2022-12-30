package test.service;

import common.db.config.KafkaConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaService {
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
    private static final Integer KAFKA_MESSAGES = 100_000_000;
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final KafkaTemplate<String, String> template;

    @Autowired
    public KafkaService(final KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void pushKafkaData() {
        scheduler.schedule(this::push, 3, TimeUnit.SECONDS);
    }

    void push() {
        StopWatch watch = new StopWatch();
        watch.start();

        for (int i = 0; i < KAFKA_MESSAGES; i++) {
            template.send(KafkaConfig.CLICKHOUSE_TOPIC, "Сообщение №" + i);
        }

        watch.stop();
        log.info("Кафка сожрала {} записей за {} ms", KAFKA_MESSAGES, watch.getLastTaskTimeMillis());
    }
}
