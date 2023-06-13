package dev.bilal.movies.review;

import dev.bilal.movies.movie.Movie;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    public Review createReview(String reviewBody, String imdbId) {
        Review review = reviewRepository.insert(new Review(reviewBody));

        mongoTemplate.update(Movie.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("reviewIds").value(review))
                .first();

        return review;
    }

    public void deleteReviewFromMovie(String imdbId, String reviewId) {
        Query movieQuery = new Query(Criteria.where("imdbId").is(imdbId));
        Update movieUpdate = new Update().pull("reviewIds", reviewId);
        mongoTemplate.updateFirst(movieQuery, movieUpdate, Movie.class);

        Query reviewQuery = new Query(Criteria.where("id").is(reviewId));
        mongoTemplate.remove(reviewQuery, Review.class);
    }
}
