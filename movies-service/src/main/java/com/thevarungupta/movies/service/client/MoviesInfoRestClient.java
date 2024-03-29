package com.thevarungupta.movies.service.client;

import com.thevarungupta.movies.service.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MoviesInfoRestClient {

    private WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public MoviesInfoRestClient(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<MovieInfo> retriveMovieInfo(String movieId){
        var url = moviesInfoUrl.concat("/{id}");
        return webClient
                .get()
                .uri(url, movieId)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .log();
    }

}
