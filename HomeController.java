package com.cinemaclub.controller;

import com.cinemaclub.model.Movie;
import com.cinemaclub.model.Review;
import com.cinemaclub.repository.MovieRepository;
import com.cinemaclub.repository.ReviewRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class HomeController {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public HomeController(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("movies", movieRepository.findAll());
        return "index";
    }

    @GetMapping("/movies/{id}")
    public String movieDetails(@PathVariable Long id, Model model) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (!movieOpt.isPresent()) {
            return "redirect:/";
        }
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("reviews", reviewRepository.findByMovieId(id));
        model.addAttribute("reviewForm", new Review());
        return "movie-details";
    }

    @PostMapping("/movies/{id}/reviews")
    public String addReview(@PathVariable Long id,
                            @ModelAttribute("reviewForm") @Valid Review review,
                            BindingResult bindingResult,
                            Model model) {
        Optional<Movie> movieOpt = movieRepository.findById(id);
        if (!movieOpt.isPresent()) {
            return "redirect:/";
        }
        Movie movie = movieOpt.get();

        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movie);
            model.addAttribute("reviews", reviewRepository.findByMovieId(id));
            return "movie-details";
        }

        review.setMovie(movie);
        reviewRepository.save(review);
        return "redirect:/movies/" + id;
    }

    @GetMapping("/movies/new")
    public String newMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movie-form";
    }

    @PostMapping("/movies")
    public String createMovie(@ModelAttribute("movie") Movie movie) {
        movieRepository.save(movie);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

