package net.pladema.dates.configuration;

import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface LocalDateMapper {

	default LocalDate fromLocalDateTime(LocalDateTime localDateTime) {
		return localDateTime != null ? localDateTime.toLocalDate() : null;
	}
	
	default LocalDateTime fromLocalDate(LocalDate localDate) {
		return localDate!= null ? LocalDateTime.of(localDate, LocalTime.now()) : null; 	
    }

	default LocalDate fromStringToLocalDate(String date) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.DATE_FORMAT ));
	}

	default LocalDateTime fromStringToLocalDateTime(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.DATE_TIME_FORMAT ));
	}

	default String fromLocalDateToString(LocalDate date) {
		return date.format(DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_FORMAT));
	}

	default String fromLocalDateTimeToString(LocalDateTime localDateTime) {
		return localDateTime.format(DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
	}
}
