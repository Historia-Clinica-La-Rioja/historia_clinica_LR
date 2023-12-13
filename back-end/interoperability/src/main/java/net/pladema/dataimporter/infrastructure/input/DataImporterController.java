package net.pladema.dataimporter.infrastructure.input;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.dataimporter.infrastructure.output.GenericTableDataImporterRepository;

import org.hl7.fhir.utilities.CSVReader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/importer")
@Tag(name = "Table data Importer", description = "Table data Importer")
@Slf4j
@RequiredArgsConstructor
public class DataImporterController {

	private final GenericTableDataImporterRepository genericTableDataImporterRepository;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadCsvData(@RequestParam("tableName") String tableName, @RequestParam("file") MultipartFile file) {
		log.debug("Input parameters -> tableName {}, file {}", tableName, file);
		try (InputStream inputStream = file.getInputStream(); CSVReader reader = new CSVReader(inputStream)) {
			Object[] readData = readCsvFile(reader);
			genericTableDataImporterRepository.saveGenericTableData(tableName, readData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Object[] readCsvFile(CSVReader reader) throws IOException {
		Object[] result = new Object[2];
		List<String> resultList = new ArrayList<>();
		String columnNames = formatColumnNames(String.join(",", reader.parseLine()));
		String[] tuple;
		while (!Objects.equals((tuple = reader.parseLine())[0], "")) {
			for (int index = 0; index < tuple.length; index++) {
				if (tuple[index].equals(""))
					tuple[index] = "null";
				if (tuple[index].equalsIgnoreCase("TRUE"))
					tuple[index] = "true";
				if (tuple[index].equalsIgnoreCase("FALSE"))
					tuple[index] = "false";
				else
					tuple[index] = "'".concat(tuple[index]).concat("'");
			}
			String formattedData = formatData(tuple);
			resultList.add(formattedData);
		}
		result[0] = columnNames;
		result[1] = resultList;
		log.debug("Output -> {}", (Object) result);
		return result;
	}

	private String formatColumnNames(String columnData) {
		log.debug("Input parameters -> columnData {}", columnData);
		String[] columnNames = columnData.split(",");
		for (int index = 0; index < columnNames.length; index++)
			columnNames[index] =  "\"".concat(columnNames[index]).concat("\"");
		return formatData(columnNames);
	}

	private String formatData(String[] data) {
		log.debug("Input parameter -> data {}", (Object) data);
		return "(".concat(String.join(",", data)).concat(")");
	}

}
