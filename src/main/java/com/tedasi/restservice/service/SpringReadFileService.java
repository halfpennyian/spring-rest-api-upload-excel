package com.tedasi.restservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tedasi.restservice.model.Location;

public interface SpringReadFileService {

	List<Location> findAll();

	boolean saveDataFromUploadFile(MultipartFile file);
		
}
