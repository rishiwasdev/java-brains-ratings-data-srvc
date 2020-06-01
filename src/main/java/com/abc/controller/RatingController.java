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
import com.abc.config.ObjectMapperConfig;
import com.abc.dto.ClientResponse;
import com.abc.dto.RatingDto;
import com.abc.service.RatingService;
import com.abc.test.DummyData;

@RestController
@RequestMapping("/movie-ratings")
public class RatingController {
	private static final Logger log = LoggerFactory.getLogger(RatingController.class);
	@Autowired
	private RatingService ratingService;
	@Autowired
	private ObjectMapperConfig mapper;

	@GetMapping("")
	private ResponseEntity<ClientResponse> getRatings() {
		return createResponse(ratingService.getAllRatings()); // TODO - Check for exceptions
	}

	@GetMapping("/as-response-entity/{movieId}")
	private Object getRating(@Valid @PathVariable("movieId") String movieId) {
		ClientResponse response = ratingService.getRating(movieId);
		log.info("# Returning Rating Response: {}", mapper.toJson(response.getData()));
		return createResponse(response); // TODO - Check for exceptions
	}

	@GetMapping("/as-response-entity/users/{userId}")
	private ResponseEntity<ClientResponse> getUserRating(@Valid @PathVariable("userId") String userId) {
		ClientResponse response = ratingService.getUserRating(userId); // TODO - Add userId field to Rating entity and DTO.
		log.info("# Returning Rating Response: {}", mapper.toJson(response.getData()));
		return createResponse(response); // TODO - Check for exceptions
	}

	@GetMapping("/as-client-response/users/{userId}")
	private ClientResponse getUserRatingAsList(@Valid @PathVariable("userId") String userId) {
		ClientResponse response = ratingService.getUserRatingAsClientResponse(userId);
		return response; // TODO - Check for exceptions
	}

	@PostMapping("/")
	private ResponseEntity<ClientResponse> addRating(@Valid @RequestBody RatingDto dto) {
		// TODO ------- REMOVE THIS ------- Start
		int totalSaved = getSavedRatings().size();
		if (totalSaved >= Constants.MAX_ITEMS_TO_SAVE) {
			log.error("Enough sample movie-ratings(" + totalSaved + ") stored.");
			return createResponse(null);
		}
		// TODO ------- REMOVE THIS ------- End
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
