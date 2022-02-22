package com.thevarungupta.movies.review.service.router;

import com.thevarungupta.movies.review.service.handler.ReviewHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class ReviewRouter {

//    @Bean
//    public RouterFunction<ServerResponse> reviewsRouter(ReviewHandler reviewHandler){
//        return route()
//                .GET("/api/v1/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World")))
//                .POST("/api/v1/reviews", request ->  reviewHandler.addReview(request))
//                .build();
//    }


    @Bean
    public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler) {
        return route()
                .nest(path("/api/v1/reviews"), builder -> {
                    builder.POST("", request -> reviewHandler.addReview(request))
                            .GET("", request -> reviewHandler.getReviews(request))
                            .PUT("/{id}", request -> reviewHandler.updateReview(request))
                            .DELETE("/{id}", request -> reviewHandler.deleteReview(request));
                }).build();
    }
}
