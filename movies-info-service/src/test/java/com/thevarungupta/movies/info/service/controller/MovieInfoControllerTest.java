package com.thevarungupta.movies.info.service.controller;

import com.thevarungupta.movies.info.service.domain.MovieInfo;
import com.thevarungupta.movies.info.service.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoService movieInfoService;

    static String MOVIES_INFO_URL = "/api/v1/movieinfos";

    @Test
    void getAllMoviesInfo() {
        var movieInfos = List.of(new MovieInfo(null, "movie 1", 2020,
                        List.of("Actio 1", "Actor 2"), LocalDate.parse("2020-01-01")),
                new MovieInfo(null, "movie 1", 2020,
                        List.of("Actio 1", "Actor 2"), LocalDate.parse("2020-01-01")),
                new MovieInfo(null, "movie 1", 2020,
                        List.of("Actio 1", "Actor 2"), LocalDate.parse("2020-01-01")));

        when(movieInfoService.getAllMovieInfo()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient
                .get()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    void addMovieInfo(){
        var movieInfo = new MovieInfo(null, "Movie 1", 2020,
                List.of("Actor 1", "Actor 2"), LocalDate.parse("2020-01-01"));
        when(movieInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(
                Mono.just(new MovieInfo("mockId", "Movie 1", 2020,
                        List.of("Actor 1", "Actor 2"), LocalDate.parse("2020-01-01")))
        );

        webTestClient
                .post()
                .uri(MOVIES_INFO_URL)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert savedMovieInfo != null;
                    assert savedMovieInfo.getMovieInfoId() != null;
                    assertEquals("mockId", savedMovieInfo.getMovieInfoId());
                });
    }

}