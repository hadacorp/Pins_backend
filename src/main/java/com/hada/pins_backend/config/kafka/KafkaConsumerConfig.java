package com.hada.pins_backend.config.kafka;


import com.hada.pins_backend.chatting.model.ChatMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by parksuho on 2022/03/17.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> meetingConsumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "meeting-listener");
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurations.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // AUTO_OFFSET_RESET_CONFIG에는 latest(가장 최근에 생성된 메시지를 offset reset), earliest(가장 오래된 메시지를), none의 값을 입력할 수 있음
        configurations.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return configurations;
    }

    @Bean
    public ConsumerFactory<String, ChatMessage> meetingConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                meetingConsumerConfigurations(), new StringDeserializer(), new JsonDeserializer<>(ChatMessage.class));
    }

    @Bean("meetingContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, ChatMessage> meetingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(meetingConsumerFactory());
        factory.setConcurrency(3); // 쓰레드 갯수
//        factory.setRetryTemplate(retryTemplate());

        ContainerProperties properties = factory.getContainerProperties();
        properties.setAckMode(ContainerProperties.AckMode.MANUAL);
        // 디폴트 값은 자동 커밋. 메시지 수신 후 자동으로 커밋, 잘 수신했다고 알려줌.
        // MANUAL 로 하면 내가 잘 수신했다고 알려줘야 함. 알려주지 않으면 카프카 큐에 계속 남아있음.
        // 잘 수신됐다고 알려주면 다음 메시지 넘겨줌.

        return factory;
    }

    @Bean
    public Map<String, Object> communityConsumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, "community-listener"); // consumer group id 설정
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurations.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        configurations.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return configurations;
    }

    @Bean
    public ConsumerFactory<String, ChatMessage> communityConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                communityConsumerConfigurations(), new StringDeserializer(), new JsonDeserializer<>(ChatMessage.class));
    }

    @Bean("communityContainerFactory")
    ConcurrentKafkaListenerContainerFactory<String, ChatMessage> communityKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ChatMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(communityConsumerFactory());
        factory.setConcurrency(3);

        ContainerProperties properties = factory.getContainerProperties();
        properties.setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

//    private RetryTemplate retryTemplate() {
//        RetryTemplate retryTemplate = new RetryTemplate();
//
//        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
//        fixedBackOffPolicy.setBackOffPeriod(3000L); // 3초 대기 후 재시도
//        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
//
//        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
//        simpleRetryPolicy.setMaxAttempts(5); // 5번 재시도
//        retryTemplate.setRetryPolicy(simpleRetryPolicy);
//
//        return retryTemplate;
//    }
}
