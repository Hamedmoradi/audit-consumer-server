package audit.consumer.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableTransactionManagement
@Slf4j
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaBootstrapAddress;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String kafkaConsumerEnableAutoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String kafkaConsumerAutoCommitInterval;

    @Value("${session.timeout.ms}")
    private Integer sessionTimeoutMs;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String kafkaConsumerAutoOffsetReset;

    @Value("${spring.kafka.consumer.group-id}")
    private String kafkaConsumerGroupId;

    @Value("${spring.kafka.consumer.isolation-level}")
    private String kafkaConsumerIsolationLevel;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String kafkaKeyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String kafkaValueDeserializer;


    @Bean
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(
            ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, consumerFactory);
        return factory;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapAddress);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaConsumerEnableAutoCommit);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaConsumerAutoCommitInterval);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerAutoOffsetReset);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);
        propsMap.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, kafkaConsumerIsolationLevel);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaKeyDeserializer);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaValueDeserializer);
        return propsMap;
    }

}