package net.pladema.snowstorm.services.loadCsv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.InputStreamSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SnomedConceptsCsvReader {

	public static Integer getTotalRecords(InputStreamSource file) {
		try {
			return getTotalRecords(file.getInputStream());
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
		}
	}
    public static Integer getTotalRecords(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser.getRecords().size();
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }

	public static List<SnomedConceptBo> csvToSnomedConceptsBo(InputStream is) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			 CSVParser csvParser = new CSVParser(fileReader,
					 CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
			List<SnomedConceptBo> concepts = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				String term = csvRecord.get("term");
				term = (isSurroundedByQuotes(term)) ? deleteSurroundingCharacters(term) : term;
				SnomedConceptBo concept = new SnomedConceptBo(
						csvRecord.get("conceptId"),
						term
				);
				concepts.add(concept);
			}
			return concepts;
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
		}
	}

	public static List<SnomedConceptBo> csvToSnomedConceptsBo(InputStream is, int start, int end) {
		try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			 CSVParser csvParser = new CSVParser(fileReader,
					 CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
			List<SnomedConceptBo> concepts = new ArrayList<>();
			Iterable<CSVRecord> csvRecords = csvParser.getRecords().subList(start, end);
			for (CSVRecord csvRecord : csvRecords) {
				String term = csvRecord.get("term");
				term = (isSurroundedByQuotes(term)) ? deleteSurroundingCharacters(term) : term;
				SnomedConceptBo concept = new SnomedConceptBo(
						csvRecord.get("conceptId"),
						term
				);
				concepts.add(concept);
			}
			return concepts;
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
		}
	}

    private static boolean isSurroundedByQuotes(String s) {
        return s.startsWith("\"") && s.endsWith("\"");
    }

    private static String deleteSurroundingCharacters(String s) {
        return s.substring(1, s.length() - 1);
    }

}
