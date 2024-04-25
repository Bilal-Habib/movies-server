package dev.bilal.movies.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    private static final String REGION = "eu-west-2";
    private static final String SERVICE_ENDPOINT = String.format("dynamodb.%s.amazonaws.com", REGION);

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDb());
    }

    @Bean
    public AmazonDynamoDB buildAmazonDynamoDb() {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    SERVICE_ENDPOINT,
                    REGION
                )
            )
            .build();
    }

}
