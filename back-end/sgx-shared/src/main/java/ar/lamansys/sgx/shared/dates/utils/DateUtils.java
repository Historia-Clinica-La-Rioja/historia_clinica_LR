package ar.lamansys.sgx.shared.dates.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.dates.exceptions.DateParseException;

public class DateUtils {

	private DateUtils() { }

	private static final int WEEK_LENGTH = 7;

	public static Short getWeekDay(LocalDate date) {
		return (short) (date.getDayOfWeek().getValue() % WEEK_LENGTH);
	}

	public static boolean isBetween(LocalTime hour, LocalTime from, LocalTime to ) {
		return hour.compareTo(from)>=0 && hour.isBefore(to);
	}

	public static LocalDate fromStringToLocalDate(String date) {
		if (date == null)
			return null;
		try {
			return LocalDate.parse(date, DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_FORMAT));
		} catch (Exception e) {
			throw new DateParseException(date, e);
		}
	}
}
