package dev.bilal.movies.movie;


import dev.bilal.movies.review.Review;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    Movie getMovie() {
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

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        var movie = getMovie();
        movieRepository.insert(movie);
    }

    @Test
    void getAllMovies() throws Exception {
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }

    @Test
    void getSingleMovie() throws Exception {
        String movieId = getMovie().getImdbId();
        String url = "/api/v1/movies/{imdbId}";
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(url, movieId));

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Movie responseMovie = new ObjectMapper().readValue(responseBody, Movie.class);

        Assertions.assertEquals(movieId, responseMovie.getImdbId());
    }

}
