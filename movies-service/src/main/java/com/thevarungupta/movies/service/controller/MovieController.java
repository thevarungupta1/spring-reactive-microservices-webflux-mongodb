package com.thevarungupta.movies.service.controller;

import com.thevarungupta.movies.service.client.MoviesInfoRestClient;
import com.thevarungupta.movies.service.client.ReviewRestClient;
import com.thevarungupta.movies.service.domain.Movie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private MoviesInfoRestClient moviesInfoRestClient;
    private ReviewRestClient reviewRestClient;

    public MovieController(MoviesInfoRestClient moviesInfoRestClient, ReviewRestClient reviewRestClient){
        this.moviesInfoRestClient = moviesInfoRestClient;
        this.reviewRestClient = reviewRestClient;
    }


    @GetMapping("/{id}")
    public Mono<Movie> retriveMovieById(@PathVariable("id") String movieId){
        return moviesInfoRestClient.retriveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    var reviewListMono =  reviewRestClient.retrieveReviews(movieId)
                            .collectList();
                    return reviewListMono.map(reviews -> new Movie(movieInfo, reviews));
                });

    }
}
