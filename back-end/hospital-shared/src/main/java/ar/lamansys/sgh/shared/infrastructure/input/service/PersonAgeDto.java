package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonAgeDto {

	private Short years;
	private Short months;
	private Short days;
	private Long totalDays;


	public PersonAgeDto(LocalDate personBirthDate){
		LocalDate today = LocalDate.now();
		Period p = Period.between(personBirthDate, today);
		long totalDays = ChronoUnit.DAYS.between(personBirthDate, today);
		this.years = (short) p.getYears();
		this.months = (short) p.getMonths();
		this.days = (short) p.getDays();
		this.totalDays = totalDays;
	}

}
