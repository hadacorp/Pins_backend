package com.hada.pins_backend.config;

import com.hada.pins_backend.chatting.model.ChatMessage;
import com.hada.pins_backend.chatting.service.RedisSubService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
 * Created by parksuho on 2022/03/25.
 * Created by parksuho on 2022/03/27.
 */
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    @Value("${cloud.redis.host}")
    private String redisHost;

    @Value("${cloud.redis.port}")
    private String redisPort;

    @Value("${cloud.redis.password}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
        redisStandaloneConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Qualifier("subRedisTemplate")
    public RedisTemplate<String, String> subRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }

    @Bean
    @Primary
    public RedisTemplate<String, ChatMessage> chatMessageRedisTemplate() {
        RedisTemplate<String, ChatMessage> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        return redisTemplate;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(MessageListenerAdapter messageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener, meetingTopic());
        container.addMessageListener(messageListener, communityTopic());
        return container;
    }

    @Bean
    public MessageListenerAdapter messageListener(RedisSubService redisSubService) {
        return new MessageListenerAdapter(redisSubService);
    }

    @Bean
    public ChannelTopic meetingTopic() {
        return new ChannelTopic("meeting");
    }

    @Bean
    public ChannelTopic communityTopic() {
        return new ChannelTopic("community");
    }
}
