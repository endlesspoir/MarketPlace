package com.marketplace.userservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    @Value("${amazon.endpoint}")
    private String endpoint;

    @Value("${amazon.access-key}")
    private String accessKey;

    @Value("${amazon.secret-key}")
    private String secretKey;

    @Value("${amazon.bucket}")
    private String bucket;

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withPathStyleAccessEnabled(true)
                .build();

    }

    @Bean
    public String amazonBucket() {
        return bucket;
    }
}
