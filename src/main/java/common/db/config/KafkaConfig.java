package common.db.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    public static final String CLICKHOUSE_TOPIC = "clickhouse_topic";
    public static final String CLICKHOUSE_GROUP = "clickhouse_group";
    private static final Byte PARTITIONS = 10;
    private static final Byte REPLICAS = 3;

    @Value("${kafka.servers}")
    private String servers;

    @Bean
    public KafkaTemplate<String, String> clickhouseKafkaTemplate() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        config.put(ProducerConfig.BATCH_SIZE_CONFIG, 1_000_000);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
        config.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, Integer.MAX_VALUE);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.ACKS_CONFIG, "all");
        config.put(ProducerConfig.RETRIES_CONFIG, 5);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // устранение дублей
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 300000);
        config.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);

        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config));
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userTopic() {
        return TopicBuilder.name(CLICKHOUSE_TOPIC)
                .partitions(PARTITIONS)
                .replicas(REPLICAS)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
                .build();
    }
}
