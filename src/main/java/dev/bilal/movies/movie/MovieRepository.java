package dev.bilal.movies.movie;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MovieRepository {

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Optional<Movie> findMovieByImdbId(String imdbId) {
        return Optional.of(dynamoDBMapper.load(Movie.class, imdbId));
    }

    public List<Movie> getAllMovies() {
        ScanRequest scanRequest = new ScanRequest().withTableName("movies");
        final var scanResult = amazonDynamoDB.scan(scanRequest);

        return dynamoDBMapper.marshallIntoObjects(Movie.class, scanResult.getItems());
    }

}
