package dev.bilal.movies.review;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review createReview(String reviewBody, String movieId) {
        return reviewRepository.createReview(reviewBody, movieId);
    }

    public void deleteReviewFromMovie(String movieId, String reviewId) {
        reviewRepository.deleteReviewFromMovie(movieId, reviewId);
    }

    public List<Review> allReviews() {
        return reviewRepository.findAll();
    }
}
