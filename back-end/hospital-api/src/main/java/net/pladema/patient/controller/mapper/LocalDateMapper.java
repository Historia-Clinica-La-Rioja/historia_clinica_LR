package net.pladema.patient.controller.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.mapstruct.Mapper;

@Mapper
public interface LocalDateMapper {

	default LocalDate fromLocalDateTime(LocalDateTime localDateTime) {
		return localDateTime != null ? localDateTime.toLocalDate() : null;
	}
	
	default LocalDateTime fromLocalDate(LocalDate localDate) {
		return localDate!= null ? LocalDateTime.of(localDate, LocalTime.now()) : null; 	
    }
}
