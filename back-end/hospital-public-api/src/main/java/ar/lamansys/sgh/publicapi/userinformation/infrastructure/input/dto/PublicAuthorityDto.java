package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicAuthorityDto {

	private Short id;

	private Integer institution;

	private String description;

	public PublicAuthorityDto(Short id, Integer institution, String description) {
		this.id = id;
		this.institution = institution;
		this.description = description;
	}
}
