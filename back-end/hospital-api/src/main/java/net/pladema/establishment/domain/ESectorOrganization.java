package net.pladema.establishment.domain;

import lombok.Getter;

@Getter
public enum ESectorOrganization {

	SERVICES (1,"Servicios"),
	PROGRESSIVE_CARE (2,"Cuidados progresivos");

	private final Short id;
	private final String description;

	ESectorOrganization(Number id, String description){
		this.id = id.shortValue();
		this.description = description;
	}
}
