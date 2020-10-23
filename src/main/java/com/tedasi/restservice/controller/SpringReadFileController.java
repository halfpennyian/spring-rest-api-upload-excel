package com.tedasi.restservice.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tedasi.restservice.model.Location;
import com.tedasi.restservice.service.SpringReadFileService;

@Controller
public class SpringReadFileController {
	
	@Autowired 
	private SpringReadFileService springReadFileService;
	
	@GetMapping(value="/")
	public String pinpoint(Model model) {
		model.addAttribute("location", new Location());
		List<Location> locations = springReadFileService.findAll();
		model.addAttribute("locations", locations);
		return "view/locations";
	}
	
	@GetMapping(value="map")
	public String map(Model model) {
		model.addAttribute("location", new Location());
		List<Location> locations = springReadFileService.findAll();
		model.addAttribute("locations", locations);
		return "view/map";
	}
	
	@PostMapping(value="/fileupload")
	private String uploadFile(@ModelAttribute Location location, RedirectAttributes redirectAttributes) {
		boolean isFlag = springReadFileService.saveDataFromUploadFile(location.getFile());
		if(isFlag) {
			redirectAttributes.addFlashAttribute("successMessage", "File Uploaded Successfully");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "File Upload Failed, Please try again");
		}
		return "redirect:/";
	}

}
