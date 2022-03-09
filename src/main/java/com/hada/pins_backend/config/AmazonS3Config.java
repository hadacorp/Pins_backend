package com.hada.pins_backend.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.core.credentials.CredentialsProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by bangjinhyuk on 2022/01/15.
 * Modified by parksuho on 2022/02/07.
 */
@Configuration
@Profile("prod")
@Slf4j
public class AmazonS3Config {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean(name = CredentialsProviderFactoryBean.CREDENTIALS_PROVIDER_BEAN_NAME)
    AWSCredentialsProvider awsCredentialsProvider() {
        return new DefaultAWSCredentialsProviderChain();
    }

    @Bean
    public AmazonS3Client amazonS3Client() {
        log.info("Connect to S3");
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                .build();
    }
}
