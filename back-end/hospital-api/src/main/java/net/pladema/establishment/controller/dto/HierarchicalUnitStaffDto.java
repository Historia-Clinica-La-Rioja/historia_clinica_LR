package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HierarchicalUnitStaffDto {

	private Integer id;
	private boolean responsible;
	private Integer hierarchicalUnitId;
	private String hierarchicalUnitAlias;
}

