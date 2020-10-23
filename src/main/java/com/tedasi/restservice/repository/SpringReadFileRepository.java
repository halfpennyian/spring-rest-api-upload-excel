package com.tedasi.restservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tedasi.restservice.model.Location;


@Repository
public interface SpringReadFileRepository extends CrudRepository<Location, Long> {

	
	
}
