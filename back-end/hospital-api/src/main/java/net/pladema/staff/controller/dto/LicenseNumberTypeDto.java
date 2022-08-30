package net.pladema.staff.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LicenseNumberTypeDto implements Serializable {

	private static final long serialVersionUID = 6549868927869015722L;

	private Short id;

	private String description;

	public LicenseNumberTypeDto(Short id, String description) {
		this.id = id;
		this.description = description;
	}

}
