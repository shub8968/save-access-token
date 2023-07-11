package com.redcarpetup.saveaccesstoken.dynamodb;



import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class SaveToDynamoDB {

    @Value("${aws.auth.accessKeyId}")
    private String accessKeyId;
    @Value("${aws.auth.secretAccessKey}")
    private String secretAccessKey;

    public void saveGitHubErrorToDynamoDB(GitHubError gitHubError) {
        AwsCredentialsProvider awsCredentialsProviders = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        "AKIAS7V3LZJHHDKLTJPS", "tAkaX0PT132JqXbKZ+SXUkQ/2bK4u6t3GmlBHc7K"));
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1).credentialsProvider(awsCredentialsProviders)
                .build();
        Map<String, AttributeValue> item = new HashMap<>();
        String itemId = UUID.randomUUID().toString();
        item.put("id", AttributeValue.builder().s(itemId).build());
        item.put("error", AttributeValue.builder().s(gitHubError.getError()).build());
        item.put("error_description", AttributeValue.builder().s(gitHubError.getErrorDescription()).build());
        item.put("error_uri", AttributeValue.builder().s(gitHubError.getErrorUri()).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("github_error_table")
                .item(item)
                .build();
        System.out.println("Data saved to github_error_table");
        PutItemResponse putItemResponse = dynamoDbClient.putItem(putItemRequest);
    }

    public void saveAccessTokenToDynamoDB(AccessToken accessToken) throws URISyntaxException {

        AwsCredentialsProvider awsCredentialsProviders = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                        "AKIAS7V3LZJHHDKLTJPS", "tAkaX0PT132JqXbKZ+SXUkQ/2bK4u6t3GmlBHc7K"));

        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.AP_SOUTH_1).credentialsProvider(awsCredentialsProviders).endpointOverride(URI.create("https://database-tokenserviceapp.cayq04pvfhsb.ap-south-1.rds.amazonaws.com"))
                .build();


        Map<String, AttributeValue> item = new HashMap<>();
        String itemId = UUID.randomUUID().toString();
        item.put("id", AttributeValue.builder().s(itemId).build());
        item.put("access_token", AttributeValue.builder().s(accessToken.getAccessToken()).build());
        item.put("expires_in", AttributeValue.builder().n(String.valueOf(accessToken.getExpiresIn())).build());
        item.put("refresh_token", AttributeValue.builder().s(accessToken.getRefreshToken()).build());
        item.put("refresh_token_expiresIn", AttributeValue.builder().n(String.valueOf(accessToken.getRefreshTokenExpiresIn())).build());
        item.put("scope", AttributeValue.builder().s(accessToken.getScope()).build());
        item.put("token_type", AttributeValue.builder().s(accessToken.getTokenType()).build());
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("oauth_table")
                .item(item)
                .build();
        System.out.println("Data saved to access_token_table");
        PutItemResponse putItemResponse = dynamoDbClient.putItem(putItemRequest);
    }

}



