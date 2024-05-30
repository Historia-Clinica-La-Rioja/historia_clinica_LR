package net.pladema.provincialreports.reportformat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

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

}
