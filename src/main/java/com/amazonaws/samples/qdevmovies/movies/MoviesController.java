package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "id", required = false) Long id,
                           @RequestParam(value = "genre", required = false) String genre) {
        logger.info("Ahoy! Fetchin' movies with search criteria - name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        List<Movie> movies;
        boolean isSearch = (name != null && !name.trim().isEmpty()) || 
                          (id != null && id > 0) || 
                          (genre != null && !genre.trim().isEmpty());
        
        if (isSearch) {
            movies = movieService.searchMovies(name, id, genre);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
        } else {
            movies = movieService.getAllMovies();
            model.addAttribute("searchPerformed", false);
        }
        
        model.addAttribute("movies", movies);
        model.addAttribute("genres", movieService.getAllGenres());
        
        if (isSearch && movies.isEmpty()) {
            model.addAttribute("noTreasureFound", true);
        }
        
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * REST API endpoint for searching movies - returns JSON response
     * Arrr! This be the treasure map for other ships (applications) to find our movies!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Genre to filter by (optional)
     * @return JSON response with search results or error message
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre) {
        
        logger.info("Ahoy! REST API search request - name: '{}', id: {}, genre: '{}'", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                response.put("success", false);
                response.put("message", "Arrr! That ID be as useless as a compass that points south, matey! Use a positive number.");
                response.put("error", "Invalid ID parameter");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<Movie> movies = movieService.searchMovies(name, id, genre);
            
            response.put("success", true);
            response.put("movies", movies);
            response.put("count", movies.size());
            
            if (movies.isEmpty()) {
                response.put("message", "Arrr! No treasure found with those search criteria, but don't give up the hunt!");
            } else {
                response.put("message", String.format("Shiver me timbers! Found %d movies in our treasure chest!", movies.size()));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Blimey! Error during movie search: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "Arrr! Something went wrong while searchin' for treasure. Try again, matey!");
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}