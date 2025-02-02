package net.pladema.provincialreports.reportformat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.SneakyThrows;

@Service
public class DateFormat {
	@SneakyThrows
	public String reformatDate(String previousDate) {

		SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = inputFormat.parse(previousDate);
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
		return outputFormat.format(date);
	}

	public String reformatDateTwo(String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return outputFormat.format(date);

	}

	@SneakyThrows

	public String reformatDateThree(String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
		return outputFormat.format(date);
	}

	public String reformatDateFour(String previousDate) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss");
		Date date = null;
		try {
			date = inputFormat.parse(previousDate);
		} catch (ParseException e) {
			throw new RuntimeException(e);

		}

		SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		return outputFormat.format(date);

	}


	@SneakyThrows
	public String reformatDateFive(String previousDate) {

		if (previousDate != null) {
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = inputFormat.parse(previousDate);

			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
			return outputFormat.format(date);
		} else {
			return "NO ESPECIFICADO";
		}
	}

	public String dateFromYMDToDMY(String dateString) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		LocalDate date = LocalDate.parse(dateString, inputFormatter);

		return date.format(outputFormatter);
	}

	public String dateFromYMDHMSNOToDMY(String dateString) {
		if (dateString.contains(".")) {
			dateString = dateString.replaceAll("-(\\d{2})$", "-$1:00");
			String[] parts = dateString.split("\\.");
			String beforeFraction = parts[0];
			String fractionAndOffset = parts[1];
			String[] fractionAndOffsetParts = fractionAndOffset.split("-");
			StringBuilder fractionalSeconds = new StringBuilder(fractionAndOffsetParts[0]);
			String offset = fractionAndOffsetParts[1];

			while (fractionalSeconds.length() < 8) {
				fractionalSeconds.append("0");
			}

			dateString = beforeFraction + "." + fractionalSeconds + "-" + offset;
		}

		if (dateString.contains("-") && !dateString.contains(":")) {
			dateString = dateString.replaceAll("-(\\d{2})$", "-$1:00");
		}

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSXXX");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		try {
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);
			return zonedDateTime.format(outputFormatter);
		} catch (DateTimeParseException e) {
			System.out.println("Failed to parse date: " + dateString);
			return null;
		}
	}

	// new universal formatting

	private static final Locale LOCALE_ARGENTINA = new Locale("es", "AR");
	private static final List<DateTimeFormatter> INPUT_FORMATTERS = Arrays.asList(
			DateTimeFormatter.ofPattern("d/M/yyyy"),
			DateTimeFormatter.ofPattern("dd/M/yyyy"),
			DateTimeFormatter.ofPattern("d/MM/yyyy"),
			DateTimeFormatter.ofPattern("dd/MM/yyyy"),
			DateTimeFormatter.ofPattern("dd-MM-yyyy"),
			DateTimeFormatter.ofPattern("d-M-yyyy"),
			DateTimeFormatter.ofPattern("dd.MM.yyyy"),
			DateTimeFormatter.ofPattern("d.M.yyyy"),
			DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(LOCALE_ARGENTINA),
			DateTimeFormatter.ofPattern("d MMM yyyy").withLocale(LOCALE_ARGENTINA),
			DateTimeFormatter.ofPattern("yyyy-MM-dd"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.ss"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSS"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssXXX"),
			new DateTimeFormatterBuilder()
					.appendPattern("yyyy-MM-dd HH:mm:ss")
					.optionalStart()
					.appendPattern("XXX")
					.optionalEnd()
					.toFormatter(),
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSXXX"),
			DateTimeFormatter.ofPattern("yyyy/MM/dd"),
			DateTimeFormatter.ofPattern("yyyyMMdd")
	);

	public String newReformatDate(String previousDate, String outputPattern) {
		if (previousDate == null || previousDate.trim().isEmpty()) {
			return "";
		}

		previousDate = previousDate.trim();
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputPattern, LOCALE_ARGENTINA);

		for (DateTimeFormatter inputFormatter : INPUT_FORMATTERS) {
			try {
				if (inputFormatter.toString().contains("H") || inputFormatter.toString().contains("m") || inputFormatter.toString().contains("s")) {
					LocalDateTime dateTime = LocalDateTime.parse(previousDate, inputFormatter);
					return dateTime.format(outputFormatter);
				} else {
					LocalDate date = LocalDate.parse(previousDate, inputFormatter);
					return date.format(outputFormatter);
				}
			} catch (DateTimeParseException e) {
				// continue trying with the next formatter
			}
		}
		throw new RuntimeException("Failed to parse date " + previousDate + " with formatter " + outputFormatter);
	}

	public String standardizeTime(String time) {
		try {
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("H:m");
			LocalTime parsedTime = LocalTime.parse(time, inputFormatter);
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");

			return parsedTime.format(outputFormatter);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid time format: " + time, e);
		}
	}

	private String normalizeDateTime(String dateString) {
		if (dateString.contains(".")) {
			String[] parts = dateString.split("\\.");
			String beforeFraction = parts[0];
			String fractionAndOffset = parts[1];
			String[] fractionAndOffsetParts = fractionAndOffset.split("-");
			StringBuilder fractionalSeconds = new StringBuilder(fractionAndOffsetParts[0]);
			String offset = fractionAndOffsetParts[1];

			while (fractionalSeconds.length() < 9) {
				fractionalSeconds.append("0");
			}

			return beforeFraction + "." + fractionalSeconds + "-" + offset;
		}

		if (dateString.contains("-") && !dateString.contains(":")) {
			return dateString + "T00:00:00.000000000-00:00";
		}

		return dateString;
	}

	public String newDateFromYMDHMSNOToDMY(String dateString) {
		if (dateString == null) {
			return "NOT SPECIFIED";
		}

		dateString = normalizeDateTime(dateString);

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSSSXXX");
		try {
			ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);
			return zonedDateTime.format(inputFormatter);
		} catch (DateTimeParseException e) {
			throw new RuntimeException("Failed to parse date: " + dateString, e);
		}
	}

}
