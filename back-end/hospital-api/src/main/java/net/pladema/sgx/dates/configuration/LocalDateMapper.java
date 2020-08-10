package net.pladema.sgx.dates.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;

@Mapper
public interface LocalDateMapper {

	default LocalDate fromLocalDateTime(LocalDateTime localDateTime) {
		return localDateTime != null ? localDateTime.toLocalDate() : null;
	}
	
	default LocalDateTime fromLocalDate(LocalDate localDate) {
		return localDate!= null ? LocalDateTime.of(localDate, LocalTime.now()) : null; 	
    }

	default LocalDate fromStringToLocalDate(String date) {
		if (date == null)
			return null;
		return LocalDate.parse(date, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.DATE_FORMAT ));
	}

	default LocalDateTime fromStringToLocalDateTime(String date) {
		if (date == null)
			return null;
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.DATE_TIME_FORMAT));
	}

	default String fromLocalDateToString(LocalDate date) {
		if (date == null)
			return null;
		return date.format(DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_FORMAT));
	}

	default String fromLocalDateTimeToString(LocalDateTime localDateTime) {
		if (localDateTime == null)
			return null;
		return localDateTime.format(DateTimeFormatter.ofPattern(JacksonDateFormatConfig.DATE_TIME_FORMAT));
	}

	default ZonedDateTime fromLocalDateTimeToZonedDateTime(LocalDateTime localDateTime) {
		if (localDateTime == null)
			return null;
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);
		return zonedDateTime.withZoneSameInstant(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
	}
	
	default LocalTime fromStringToLocalTime(String date) {
		if (date == null)
			return null;
		return LocalTime.parse(date, DateTimeFormatter.ofPattern( JacksonDateFormatConfig.TIME_FORMAT));
	}

	default String fromLocalTimeToString(LocalTime time) {
		if (time == null)
			return null;
		return time.format(DateTimeFormatter.ofPattern(JacksonDateFormatConfig.TIME_FORMAT));
	}

}
