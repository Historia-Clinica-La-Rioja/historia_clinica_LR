package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

@Getter
public class PrescriptionLineDoesNotExistsException extends ConstraintViolationException {

	private static final long serialVersionUID = 2000848260485037066L;

	public PrescriptionLineDoesNotExistsException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(message, constraintViolations);
	}

}
