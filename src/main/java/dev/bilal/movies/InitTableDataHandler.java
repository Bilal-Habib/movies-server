package dev.bilal.movies;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class InitTableDataHandler implements RequestHandler<Object, Void> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
    private final DynamoDB dynamoDBClient = new DynamoDB(dynamoDB);

    private final static String MOVIES_TABLE_NAME = "movies";
    private final static String REVIEWS_TABLE_NAME = "reviews";

    @Override
    public Void handleRequest(Object input, Context context) {
        try {
            List<Map<String, Object>> moviesData = loadJsonDataFromFile("movies.json");
            insertDataIntoDynamoDBTable(moviesData, MOVIES_TABLE_NAME);

            List<Map<String, Object>> reviewsData = loadJsonDataFromFile("reviews.json");
            insertDataIntoDynamoDBTable(reviewsData, REVIEWS_TABLE_NAME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private List<Map<String, Object>> loadJsonDataFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (InputStream inputStream = new FileInputStream(file)) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        }
    }

    private void insertDataIntoDynamoDBTable(List<Map<String, Object>> data, String tableName) {
        Table table = dynamoDBClient.getTable(tableName);
        for (Map<String, Object> item : data) {
            table.putItem(Item.fromMap(item));
        }
    }
}