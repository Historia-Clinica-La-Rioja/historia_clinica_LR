package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

@Getter
public class PrescriptionLineInvalidStateChangeException extends ConstraintViolationException {

	private static final long serialVersionUID = 7139806345437319528L;

	public PrescriptionLineInvalidStateChangeException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(message, constraintViolations);
	}

}
