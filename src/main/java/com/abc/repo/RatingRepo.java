package com.abc.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abc.dto.RatingDto;
import com.abc.entity.Rating;

@Repository
public interface RatingRepo extends JpaRepository<Rating, Long> {
	Optional<Rating> findByMovieId(String userId);
}
