package com.thevarungupta.movies.info.service.repository;

import com.thevarungupta.movies.info.service.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(
                new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-01-01")),
                new MovieInfo(null, "Avengers", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-01-01")),
                new MovieInfo(null, "Superman", 2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-01-01"))
        );
        movieInfoRepository
                .saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository
                .deleteAll()
                .block();
    }

    @Test
    void findAll() {
        var moviesInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(moviesInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var moviesInfoMono = movieInfoRepository.findById("abc");

        StepVerifier.create(moviesInfoMono)
                //.expectNextCount(1)
                .assertNext(movieInfo -> {
                    assertEquals("Batman Begins", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {
        var movieInfo = new MovieInfo(null, "abc",
                2020, List.of("A", "B"), LocalDate.parse("20200-01-01"));

        var movieInfoMono = movieInfoRepository.save(movieInfo);

        StepVerifier.create(movieInfoMono)
                //.expectNextCount(1)
                .assertNext(movieInfo1 -> {
                    assertNotNull(movieInfo1.getMovieInfoId());
                    assertEquals("abc", movieInfo1.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {
        // given

    }

    @Test
    void deleteMovieInfo() {
        movieInfoRepository.deleteById("abc");
        var movieInfoFlux = movieInfoRepository.findAll();
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}