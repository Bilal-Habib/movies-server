package dev.bilal.movies;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InitTableDataHandlerTest {

    @Test
    void handleRequest() {
        InitTableDataHandler initTableDataHandler = new InitTableDataHandler();
        initTableDataHandler.handleRequest(null, null);
    }

}
