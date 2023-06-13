package dev.bilal.movies.review;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bilal.movies.movie.Movie;
import dev.bilal.movies.movie.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    Movie getMovieWithoutReview() {
        List<String> genres = List.of("example genre");
        List<String> backDrops = List.of("backdrop link");
        List<Review> reviewIds = new ArrayList<>();

        return new Movie(
                "123",
                "title",
                "date",
                "trailer link",
                "poster link",
                genres,
                backDrops,
                reviewIds
        );
    }

    Review getReview() {
        return new Review("First Review!");
    }

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        movieRepository.insert(getMovieWithoutReview());
        reviewRepository.deleteAll();
        reviewRepository.insert(getReview());
    }

    @Test
    void createReview() throws Exception {
        String url = "/api/v1/reviews";
        Movie movie = getMovieWithoutReview();
        Review review = getReview();

        Map<String, String> payload = new HashMap<>();
        payload.put("reviewBody", review.getBody());
        payload.put("imdbId", movie.getImdbId());

        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(payload)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Optional<Movie> movieWithReview = movieRepository.findMovieByImdbId(getMovieWithoutReview().getImdbId());
        List<Review> movieReviews = movieWithReview.get().getReviewIds();
        Assertions.assertEquals(1, movieReviews.size());
    }

}
