package net.pladema.nominatim.fetchglobalcoordinatesbyaddress.domain.enums;

import lombok.Getter;

@Getter
public enum ENominatimResponseCode {

	SERVER_ERROR((short) 1),
	NOT_FOUND((short) 2),
	SUCCESSFUL((short) 3);

	private final Short id;

	ENominatimResponseCode(Short id) {
		this.id = id;
	}

}
