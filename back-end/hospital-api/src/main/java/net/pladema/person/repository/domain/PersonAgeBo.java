package net.pladema.person.repository.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@Setter
@Getter
public class PersonAgeBo {

	private Short years;
	private Short months;
	private Short days;
	private Long totalDays;


	public PersonAgeBo(LocalDate personBirthDate){
		LocalDate today = LocalDate.now();
		Period p = Period.between(personBirthDate, today);
		long totalDays = ChronoUnit.DAYS.between(personBirthDate, today);
		this.years = (short) p.getYears();
		this.months = (short) p.getMonths();
		this.days = (short) p.getDays();
		this.totalDays = totalDays;
	}
}
