package com.abc.service;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.abc.dto.ClientResponse;
import com.abc.dto.RatingDto;

@Service
public interface RatingService {

	ClientResponse getAllRatings();

	ClientResponse getRating(String movieId);

	ClientResponse getUserRating(String userId);

	ClientResponse getUserRatingAsClientResponse(@Valid String userId);

	ClientResponse addRating(RatingDto dto);

	ClientResponse addRatings(Set<RatingDto> ratings);

}
