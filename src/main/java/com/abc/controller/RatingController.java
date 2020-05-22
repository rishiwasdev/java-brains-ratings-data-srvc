package com.abc.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abc.config.Constants;
import com.abc.dto.ClientResponse;
import com.abc.dto.RatingDto;
import com.abc.service.RatingService;
import com.abc.test.DummyData;

@RestController
@RequestMapping("/movie-rating")
public class RatingController {
	private static final Logger log = LoggerFactory.getLogger(RatingController.class);
	@Autowired
	private RatingService ratingService;

	@GetMapping("")
	private ResponseEntity<ClientResponse> getRatings() {
		return createResponse(ratingService.getAllRatings()); // TODO - Check for exceptions
	}

	@GetMapping("/{movieId}")
	private Object getRating(@Valid @PathVariable("movieId") String movieId) {
		log.debug("# movieId: {}", movieId);
		return createResponse(ratingService.getRating(movieId)); // TODO - Check for exceptions
//		return ratingService.getRating(movieId).getData();
	}

	@PostMapping("/")
	private ResponseEntity<ClientResponse> addRating(@Valid @RequestBody RatingDto dto) {
		int totalSaved = getSavedRatings().size();
		if (totalSaved >= Constants.MAX_ITEMS_TO_SAVE) {
			log.error("Enough sample movie-ratings(" + totalSaved + ") stored.");
			return createResponse(null);
		}
		log.debug("# RatingDto: {}", dto);
		dto = DummyData.randomRating();
		log.debug("# Dummy RatingDto: {}", dto);
		return createResponse(ratingService.addRating(dto)); // TODO - Check for exceptions
	}

	@PostMapping("/add-dummy-ratings")
	private ResponseEntity<ClientResponse> addDummyRatings() {
		List<RatingDto> savedRatings = getSavedRatings();
		return createResponse(ratingService.addRatings(DummyData.addRandomItems(savedRatings)));
	}

	// HELPER METHODS
	private ResponseEntity<ClientResponse> createResponse(ClientResponse res) {
		return (res == null) ? new ResponseEntity<>(res, HttpStatus.CONFLICT) : new ResponseEntity<>(res, HttpStatus.OK);
	}

	private List<RatingDto> getSavedRatings() {
		ResponseEntity<ClientResponse> entity = getRatings();
		List<RatingDto> savedRatings = (List<RatingDto>) entity.getBody().getData();
		return savedRatings;
	}
}
