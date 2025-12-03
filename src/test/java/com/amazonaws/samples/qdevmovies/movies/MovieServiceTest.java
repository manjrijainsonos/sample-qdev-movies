package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MovieService search functionality
 * Arrr! These tests be makin' sure our treasure huntin' methods work ship-shape!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        // Should load movies from movies.json
        assertTrue(movies.size() > 0);
    }

    @Test
    public void testGetMovieByIdValid() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent());
        assertEquals(1L, movie.get().getId());
        assertEquals("The Prison Escape", movie.get().getMovieName());
    }

    @Test
    public void testGetMovieByIdInvalid() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdNull() {
        Optional<Movie> movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdZero() {
        Optional<Movie> movie = movieService.getMovieById(0L);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testGetMovieByIdNegative() {
        Optional<Movie> movie = movieService.getMovieById(-1L);
        assertFalse(movie.isPresent());
    }

    @Test
    public void testSearchMoviesNoFilters() {
        List<Movie> results = movieService.searchMovies(null, null, null);
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesEmptyFilters() {
        List<Movie> results = movieService.searchMovies("", null, "");
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesByNameExact() {
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNamePartial() {
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNameCaseInsensitive() {
        List<Movie> results = movieService.searchMovies("PRISON", null, null);
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testSearchMoviesByNameNotFound() {
        List<Movie> results = movieService.searchMovies("NonExistentMovie", null, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }

    @Test
    public void testSearchMoviesByIdNotFound() {
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesByIdInvalid() {
        List<Movie> results = movieService.searchMovies(null, -1L, null);
        // Should return all movies when invalid ID is provided
        List<Movie> allMovies = movieService.getAllMovies();
        assertEquals(allMovies.size(), results.size());
    }

    @Test
    public void testSearchMoviesByGenreExact() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        assertFalse(results.isEmpty());
        // All results should contain "Drama" in genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"));
        }
    }

    @Test
    public void testSearchMoviesByGenrePartial() {
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        assertFalse(results.isEmpty());
        // All results should contain "Crime" in genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("crime"));
        }
    }

    @Test
    public void testSearchMoviesByGenreCaseInsensitive() {
        List<Movie> results = movieService.searchMovies(null, null, "ACTION");
        assertFalse(results.isEmpty());
        // All results should contain "action" in genre (case insensitive)
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("action"));
        }
    }

    @Test
    public void testSearchMoviesByGenreNotFound() {
        List<Movie> results = movieService.searchMovies(null, null, "Horror");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchMoviesMultipleFilters() {
        // Search for a specific movie using multiple criteria
        List<Movie> results = movieService.searchMovies("Family Boss", 2L, "Crime");
        assertEquals(1, results.size());
        Movie movie = results.get(0);
        assertEquals("The Family Boss", movie.getMovieName());
        assertEquals(2L, movie.getId());
        assertTrue(movie.getGenre().toLowerCase().contains("crime"));
    }

    @Test
    public void testSearchMoviesMultipleFiltersNoMatch() {
        // Search with conflicting criteria
        List<Movie> results = movieService.searchMovies("Prison", 2L, null);
        assertTrue(results.isEmpty()); // ID 2 is not "Prison" movie
    }

    @Test
    public void testSearchMoviesWithWhitespace() {
        List<Movie> results = movieService.searchMovies("  Prison  ", null, "  Drama  ");
        assertEquals(1, results.size());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        assertNotNull(genres);
        assertFalse(genres.isEmpty());
        
        // Should contain unique genres from movies.json
        assertTrue(genres.contains("Drama"));
        assertTrue(genres.contains("Crime/Drama"));
        assertTrue(genres.contains("Action/Crime"));
        
        // Should be sorted
        for (int i = 1; i < genres.size(); i++) {
            assertTrue(genres.get(i-1).compareTo(genres.get(i)) <= 0);
        }
        
        // Should not contain duplicates
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(genres.size(), uniqueCount);
    }

    @Test
    public void testSearchMoviesPerformance() {
        // Test that search doesn't take too long even with multiple calls
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            movieService.searchMovies("Test", null, null);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Should complete 100 searches in reasonable time (less than 1 second)
        assertTrue(duration < 1000, "Search performance is too slow: " + duration + "ms");
    }

    @Test
    public void testSearchMoviesConsistency() {
        // Multiple calls with same parameters should return same results
        List<Movie> results1 = movieService.searchMovies("Prison", null, null);
        List<Movie> results2 = movieService.searchMovies("Prison", null, null);
        
        assertEquals(results1.size(), results2.size());
        for (int i = 0; i < results1.size(); i++) {
            assertEquals(results1.get(i).getId(), results2.get(i).getId());
        }
    }
}