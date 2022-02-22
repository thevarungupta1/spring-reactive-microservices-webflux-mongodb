package com.thevarungupta.movies.service.client;

import com.thevarungupta.movies.service.domain.MovieInfo;
import com.thevarungupta.movies.service.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

@Component
public class ReviewRestClient {

    @Value("${restClient.reviewsUrl}")
    private String reviewUrl;

    private WebClient webClient;

    public ReviewRestClient(WebClient webClient){
        this.webClient = webClient;
    }

    public Flux<Review> retrieveReviews(String movieId){
        var url = UriComponentsBuilder.fromHttpUrl(reviewUrl)
                .queryParam("movieInfoId", movieId)
                .buildAndExpand().toUriString();
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToFlux(Review.class)
                .log();
    }

}
