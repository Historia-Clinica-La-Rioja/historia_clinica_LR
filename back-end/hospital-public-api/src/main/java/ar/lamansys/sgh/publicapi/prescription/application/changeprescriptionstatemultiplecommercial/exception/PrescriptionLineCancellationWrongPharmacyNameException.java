package ar.lamansys.sgh.publicapi.prescription.application.changeprescriptionstatemultiplecommercial.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import java.util.Set;

@Getter
public class PrescriptionLineCancellationWrongPharmacyNameException extends ConstraintViolationException {

	private static final long serialVersionUID = 7746278910060481318L;

	public PrescriptionLineCancellationWrongPharmacyNameException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
		super(message, constraintViolations);
	}

}
