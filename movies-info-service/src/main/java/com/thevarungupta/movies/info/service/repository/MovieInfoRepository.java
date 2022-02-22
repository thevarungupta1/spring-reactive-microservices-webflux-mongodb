package com.thevarungupta.movies.info.service.repository;

import com.thevarungupta.movies.info.service.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
