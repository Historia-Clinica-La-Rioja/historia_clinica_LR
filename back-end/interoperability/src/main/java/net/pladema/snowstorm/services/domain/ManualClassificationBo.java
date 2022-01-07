package net.pladema.snowstorm.services.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ManualClassificationBo {

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String description;

	public ManualClassificationBo(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
}
