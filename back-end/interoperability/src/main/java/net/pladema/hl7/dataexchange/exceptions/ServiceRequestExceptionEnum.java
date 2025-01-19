package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

@Getter
public enum ServiceRequestExceptionEnum {

	SERVICE_REQUEST_NOT_FOUND,
	SERVICE_REQUEST_DOMAIN_ID_WRONG,
	SERVICE_REQUEST_ID_WRONG_FORMAT

}
