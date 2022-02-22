package com.thevarungupta.movies.info.service.controller;

import com.thevarungupta.movies.info.service.domain.MovieInfo;
import com.thevarungupta.movies.info.service.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class MovieInfoController {

    private MovieInfoService movieInfoService;

    public MovieInfoController(MovieInfoService movieInfoService){
        this.movieInfoService = movieInfoService;
    }

    @GetMapping("/movieinfos")
    public Flux<MovieInfo> getAllMoviesInfo(){
        return movieInfoService.getAllMovieInfo();
    }

    @GetMapping("/movieinfos/{id}")
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id){
        return movieInfoService.getMovieInfoById(id);
    }

    @PostMapping("/movieinfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
        return movieInfoService.addMovieInfo(movieInfo);
    }
}
