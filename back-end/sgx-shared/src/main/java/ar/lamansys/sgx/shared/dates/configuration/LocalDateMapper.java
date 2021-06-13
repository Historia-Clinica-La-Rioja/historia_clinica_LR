package ar.lamansys.sgx.shared.dates.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.TimeDto;
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

	default LocalTime fromTimeDto(TimeDto timeDto) {
		if (timeDto == null)
			return null;
		Integer seconds = timeDto.getSeconds() != null ? timeDto.getSeconds() : Integer.valueOf(0);
		return LocalTime.of(timeDto.getHours(), timeDto.getMinutes(), seconds);
	}

	default LocalDate fromDateDto(DateDto dateDto) {
		if (dateDto == null)
			return null;
		return LocalDate.of(dateDto.getYear(), dateDto.getMonth(), dateDto.getDay());
	}

	default LocalDateTime fromDateTimeDto(DateTimeDto dateTimeDto) {
		if (dateTimeDto == null)
			return null;

		LocalTime time = fromTimeDto(dateTimeDto.getTime());
		LocalDate date = fromDateDto(dateTimeDto.getDate());
		return LocalDateTime.of(date, time);
	}

	default TimeDto toTimeDto(LocalTime time) {
		if (time == null)
			return null;
		return new TimeDto(time.getHour(), time.getMinute(), time.getSecond());
	}

	default DateDto toDateDto(LocalDate date) {
		if (date == null)
			return null;
		return new DateDto(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	}

	default DateTimeDto toDateTimeDto(LocalDateTime localDateTime) {
		if (localDateTime == null)
			return null;

		DateDto dateDto = toDateDto(localDateTime.toLocalDate());
		TimeDto timeDto = toTimeDto(localDateTime.toLocalTime());
		return new DateTimeDto(dateDto, timeDto);
	}

}
