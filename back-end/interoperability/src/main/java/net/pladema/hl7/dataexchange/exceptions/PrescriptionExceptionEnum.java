package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

@Getter
public enum PrescriptionExceptionEnum {

	PRESCRIPTION_NOT_FOUND,
	PRESCRIPTION_ID_WRONG_FORMAT,
	PRESCRIPTION_DOMAIN_ID_WRONG

}
