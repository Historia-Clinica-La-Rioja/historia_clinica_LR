package ar.lamansys.sgh.publicapi.activities.holidays.application.exception;

import java.util.Set;

import javax.validation.ConstraintViolationException;

public class JustOneDateException extends ConstraintViolationException {
	public JustOneDateException(){
		super("Si se indican fechas, se deben indicar ambas", Set.of());
	}
}
