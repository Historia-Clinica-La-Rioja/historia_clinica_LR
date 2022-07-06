package ar.lamansys.sgh.publicapi.domain.authorities;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicAuthorityBo {

	private Short id;

	private Integer institution;

	private String description;

	public PublicAuthorityBo(Short id, Integer institution, String description) {
		this.id = id;
		this.institution = institution;
		this.description = description;
	}
}
