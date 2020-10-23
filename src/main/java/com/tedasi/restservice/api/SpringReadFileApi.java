package com.tedasi.restservice.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tedasi.restservice.model.Location;
import com.tedasi.restservice.service.SpringReadFileService;

@RestController
@RequestMapping("/api")
public class SpringReadFileApi {

	@Autowired
	private SpringReadFileService springReadFileService;

	@GetMapping(path = "/data")
	public List<Location> get() {
		List<Location> locations = springReadFileService.findAll();

		return locations;
	}
}
