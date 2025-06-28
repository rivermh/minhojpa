package com.minhojpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhojpa.entity.Place;

public interface PlaceRepository extends JpaRepository<Place, Long> {

}
