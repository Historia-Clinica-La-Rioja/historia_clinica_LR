package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.exceptions;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

@Getter
public class PrescriptionCancelledWithNoObservationException extends ConstraintViolationException {

	private static final long serialVersionUID = 3569058667856781329L;

	public PrescriptionCancelledWithNoObservationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(message, constraintViolations);
	}

}
