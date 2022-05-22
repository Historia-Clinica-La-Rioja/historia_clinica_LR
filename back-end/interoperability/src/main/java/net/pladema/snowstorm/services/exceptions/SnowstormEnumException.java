package net.pladema.snowstorm.services.exceptions;

import lombok.Getter;

@Getter
public enum SnowstormEnumException {
    SNOWSTORM_TIMEOUT_SERVICE,
    SERVER_ERROR,
	NOT_FOUND,
	UNPARSEABLE_RESPONSE,
	BAD_REQUEST,
	API_TIMEOUT,
	NULL_RESPONSE,
	UNKNOWN_ERROR,
	UNPROCESSABLE_ENTITY
	;
}
