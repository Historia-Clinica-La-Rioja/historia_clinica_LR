package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HierarchicalUnitDto {

	private Integer id;

	private String name;

	private Integer typeId;

	public HierarchicalUnitDto(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

}
