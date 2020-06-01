package com.abc.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.config.ObjectMapperConfig;
import com.abc.dto.ClientResponse;
import com.abc.dto.RatingDto;
import com.abc.entity.Rating;
import com.abc.repo.RatingRepo;
import com.abc.service.RatingService;
import com.abc.test.DummyData;
import com.abc.util.Util;

@Service
public class RatingServiceImpl implements RatingService {
	private static final Logger log = LoggerFactory.getLogger(RatingServiceImpl.class);

	@Autowired
	private ObjectMapperConfig mapper;
	@Autowired
	private RatingRepo ratingRepo;

	@Override
	public ClientResponse getAllRatings() {
		List<Rating> ratings = ratingRepo.findAll();
		List<RatingDto> savedRatings = mapper.createList(mapper.toJson(ratings), RatingDto.class);
		return createClientResponse(savedRatings);
	}

	@Override
	public ClientResponse getRating(String movieId) {
		log.debug("# movieId: {}", movieId);
		Rating rating = ratingRepo.findByMovieId(movieId).orElse(null);// TODO .orElseThrow(() -> new IllegalArgumentException("No rating found for Movie Id: " + movieId));
		log.info("# Rating: {}", rating);
		return createClientResponse(mapper.convert(rating, RatingDto.class));
	}

	@Override
	public ClientResponse getUserRating(String userId) {
		log.debug("# userId: {}", userId); // TODO - RATING TABLE SHOULD HAVE COLUMN USER ID, ADD IT
		Set<String> movieIds = DummyData.dummyMovieIds(5); // TODO - REMOVE
		List<RatingDto> ratingDtos = movieIds.stream().map(id -> {
			Rating rating = ratingRepo.findByMovieId(id).orElse(null); // TODO - FIND BY USER ID, NOT MOVIE ID
			// TODO .orElseThrow(() -> new IllegalArgumentException("No rating found for Movie Id: " + movieId));
			log.info("# Rating: {}", rating);
			RatingDto ratingDto = mapper.convert(rating, RatingDto.class);
			log.info("# RatingDto: {}", ratingDto);
			return ratingDto;
		}).collect(Collectors.toList());
		log.info("# Rating-list: {}", ratingDtos);
		return createClientResponse(ratingDtos);
	}

	@Override
	public ClientResponse getUserRatingAsClientResponse(String userId) {
		// TODO - RATING TABLE SHOULD HAVE COLUMN USER ID, ADD IT
		Set<String> movieIds = DummyData.dummyMovieIds(5); // TODO - REMOVE
		List<RatingDto> ratingDtos = movieIds.stream().map(id -> {
			Rating rating = ratingRepo.findByMovieId(id).orElse(null); // TODO - FIND BY USER ID, NOT MOVIE ID
			// TODO .orElseThrow(() -> new IllegalArgumentException("No rating found for Movie Id: " + movieId));
			return mapper.convert(rating, RatingDto.class);
		}).collect(Collectors.toList());
		log.info("# Rating-list: {}", ratingDtos);
		return createClientResponse(ratingDtos);
	}

	@Override
	public ClientResponse addRating(RatingDto dto) {
		log.debug("# RatingDto: {}", dto);
		Rating rating = mapper.convert(dto, Rating.class);
		log.info("# Rating: {}", rating);
		Rating savedRating = ratingRepo.save(rating);
		log.info("# savedRating: {}", savedRating);
		RatingDto savedRatingDto = mapper.convert(savedRating, RatingDto.class);
		return createClientResponse(savedRatingDto);
	}

	@Override
	public ClientResponse addRatings(Set<RatingDto> ratingDtos) {
		if (Util.nullOrEmpty(ratingDtos)) {
			log.error("No movie-ratings provided to add.");
			return null;
		}
		log.debug("# Total ratings to add = {}", ratingDtos.size());
		Set<Rating> ratings = mapper.createSet(mapper.toJson(ratingDtos), Rating.class);
		List<Rating> savedRatings = ratingRepo.saveAll(ratings);
		log.debug("# Total ratings added = {}", savedRatings.size());
		return createClientResponse(savedRatings.size());
	}

	private ClientResponse createClientResponse(Object obj) {
		ClientResponse res = new ClientResponse();
		res.setData(obj);
		return res;
	}

}
