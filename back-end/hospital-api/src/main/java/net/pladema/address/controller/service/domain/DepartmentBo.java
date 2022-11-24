package net.pladema.address.controller.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentBo {

	private Short id;

	private String description;

	private Short provinceId;

	public DepartmentBo(Short id, String description) {
		this.id = id;
		this.description = description;
	}

}
