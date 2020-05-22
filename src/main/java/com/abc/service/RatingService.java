package com.abc.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.abc.dto.RatingDto;
import com.abc.dto.ClientResponse;

@Service
public interface RatingService {
	
	ClientResponse getAllRatings();

	ClientResponse getRating(String movieId);

	ClientResponse addRating(RatingDto dto);

	ClientResponse addRatings(Set<RatingDto> ratings);

}
