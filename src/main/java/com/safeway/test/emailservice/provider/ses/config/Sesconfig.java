package com.safeway.test.emailservice.provider.ses.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sesconfig {

    public static final String AWS_ACCESS_KEY_ID = System.getenv("AWS_ACCESS_KEY_ID");

    public static final String AWS_SECRET_KEY = System.getenv("AWS_SECRET_KEY");

    public static final String AWS_REGION = System.getenv("AWS_REGION");

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(AWS_REGION)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
