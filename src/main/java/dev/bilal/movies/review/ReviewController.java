package dev.bilal.movies.review;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Map<String, String> payload) {
        return new ResponseEntity<Review>(reviewService.createReview(payload.get("reviewBody"), payload.get("imdbId")), HttpStatus.CREATED);
    }

//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}/movies/{imdbId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("imdbId") String imdbId, @PathVariable("id") String reviewId) {
        reviewService.deleteReviewFromMovie(imdbId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
