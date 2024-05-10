package ar.lamansys.sgh.publicapi.reports.application.fetchconsultations.exceptions;

import javax.validation.ConstraintViolationException;

import java.util.Set;

public class WrongDateFilterException extends ConstraintViolationException {
	public WrongDateFilterException() {
		super("El filtro de fechas excede los 30 días", Set.of());
	}
}
