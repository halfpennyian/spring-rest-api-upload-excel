package com.tedasi.restservice.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.tedasi.restservice.model.Location;
import com.tedasi.restservice.repository.SpringReadFileRepository;

@Service
@Transactional
public class SpringReadFileServiceImpl implements SpringReadFileService {

	@Autowired
	private SpringReadFileRepository springReadFileRepository;

	@Override
	public List<Location> findAll() {
		return (List<Location>) springReadFileRepository.findAll();
	}

	@Override
	public boolean saveDataFromUploadFile(MultipartFile file) {

		boolean isFlag = false;
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		if (extension.equalsIgnoreCase("json")) {
			isFlag = readDataFromJson(file);
		} else if (extension.equalsIgnoreCase("csv")) {
			isFlag = readDataFromCsv(file);
		} else if (extension.equalsIgnoreCase("xls") || extension.equalsIgnoreCase("xlsx")) {
			isFlag = readDataFromExcel(file);
		}

		return false;
	}

	private boolean readDataFromExcel(MultipartFile file) {
		Workbook workbook = getWorkBook(file);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rows = sheet.iterator();
		rows.next();
		while (rows.hasNext()) {
			Row row = rows.next();
			Location location = new Location();
			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING) {
				location.setCoordinate(row.getCell(0).getStringCellValue());

			}
			if (row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING) {
				location.setPrice(row.getCell(1).getStringCellValue());
			}

			location.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
			springReadFileRepository.save(location);
		}

		return false;
	}

	private Workbook getWorkBook(MultipartFile file) {
		Workbook workbook = null;
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		try {
			if (extension.equalsIgnoreCase("xls")) {
				workbook = new XSSFWorkbook(file.getInputStream());
			} else if (extension.equalsIgnoreCase("xls")) {
				workbook = new HSSFWorkbook(file.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return workbook;
	}

	private boolean readDataFromCsv(MultipartFile file) {
		try {

			InputStreamReader reader = new InputStreamReader(file.getInputStream());
			CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
			List<String[]> rows = csvReader.readAll();

			for (String[] row : rows) {
				springReadFileRepository
						.save(new Location(row[0], row[1], FilenameUtils.getExtension(file.getOriginalFilename())));
			}
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	private boolean readDataFromJson(MultipartFile file) {
		try {

			InputStream inputStream = file.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			List<Location> locations = Arrays.asList(mapper.readValue(inputStream, Location[].class));
			if (locations != null && locations.size() > 0) {
				for (Location location : locations) {
					location.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
				}
			}
			return true;

		} catch (Exception e) {
			return false;
		}

	}

}
