package com.thevarungupta.movies.review.service.handler;

import com.thevarungupta.movies.review.service.domain.Review;
import com.thevarungupta.movies.review.service.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    private ReviewRepository repository;

    public ReviewHandler(ReviewRepository repository) {
        this.repository = repository;
    }


    private Mono<ServerResponse> buildReviewsResponse(Flux<Review> reviewFlux) {
        return ServerResponse.ok().body(reviewFlux, Review.class);
    }


    // add review
    public Mono<ServerResponse> addReview(ServerRequest request) {
        return request.bodyToMono(Review.class)
                .flatMap(review -> repository.save(review))
                .flatMap(savedReview -> ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(savedReview));
    }

    // get review
    public Mono<ServerResponse> getReviews(ServerRequest request) {
        var movieInfoId = request.queryParam("movieInfoId");
        if (movieInfoId.isPresent()) {
            var reviewsFlux = repository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
            return buildReviewsResponse(reviewsFlux);
        } else {
            var reviewFlux = repository.findAll();
            return buildReviewsResponse(reviewFlux);
        }
    }

    // update review
    public Mono<ServerResponse> updateReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId);

        return existingReview
                .flatMap(review -> request.bodyToMono(Review.class)
                        .map(reqReview -> {
                            review.setComment(reqReview.getComment());
                            review.setRating(reqReview.getRating());
                            return review;
                        })
                        .flatMap(repository::save)
                        .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // delete review
    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        var reviewId = request.pathVariable("id");
        var existingReview = repository.findById(reviewId);
        return existingReview
                .flatMap(review -> repository.deleteById(reviewId))
                .then(ServerResponse.noContent().build());
    }
}
