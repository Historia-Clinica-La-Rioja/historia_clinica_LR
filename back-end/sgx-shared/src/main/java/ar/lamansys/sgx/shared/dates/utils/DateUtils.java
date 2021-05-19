package ar.lamansys.sgx.shared.dates.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtils {

	private static final int WEEK_LENGTH = 7;

	public static Short getWeekDay(LocalDate date) {
		return (short) (date.getDayOfWeek().getValue() % WEEK_LENGTH);
	}

	public static boolean isBetween(LocalTime hour, LocalTime from, LocalTime to ) {
		return hour.compareTo(from)>=0 && hour.isBefore(to);
	}
	
}
