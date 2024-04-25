package dev.bilal.movies.review;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import dev.bilal.movies.movie.Movie;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public Review createReview(String reviewBody, String movieId) {
        Review review = insertReviewIntoReviewTable(reviewBody);
        updateReviewsListForMovie(movieId, review);

        return review;
    }

    private Review insertReviewIntoReviewTable(String reviewBody) {
        Review review = new Review();
        review.setBody(reviewBody);
        dynamoDBMapper.save(review);
        return review;
    }

    private void updateReviewsListForMovie(String movieId, Review review) {
        Movie movie = dynamoDBMapper.load(Movie.class, movieId);
        if (movie.getReviewIds() == null) {
            movie.setReviewIds(new ArrayList<>());
        }
        movie.getReviewIds().add(review.getReviewId());
        dynamoDBMapper.save(movie);
    }

    public void deleteReviewFromMovie(String movieId, String reviewId) {
        deleteReviewFromReviewTable(reviewId);
        deleteReviewFromMovieTable(movieId, reviewId);
    }

    private void deleteReviewFromReviewTable(String reviewId) {
        dynamoDBMapper.delete(dynamoDBMapper.load(Review.class, reviewId));
    }

    private void deleteReviewFromMovieTable(String movieId, String reviewId) {
        Movie movie = dynamoDBMapper.load(Movie.class, movieId);
        movie.getReviewIds().remove(reviewId);
        dynamoDBMapper.save(movie);
    }

    public List<Review> findAll() {
        ScanRequest scanRequest = new ScanRequest().withTableName("reviews");
        final var scanResult = amazonDynamoDB.scan(scanRequest);

        return dynamoDBMapper.marshallIntoObjects(Review.class, scanResult.getItems());
    }
}
