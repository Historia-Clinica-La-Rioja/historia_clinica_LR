package ar.lamansys.sgh.publicapi.activities.holidays.application.exception;

import javax.validation.ConstraintViolationException;

import java.util.Set;

public class WrongDatesException extends ConstraintViolationException {
	public WrongDatesException(){
		super("La fecha de fin debe ser posterior a la fecha de inicio", Set.of());
	}
}
