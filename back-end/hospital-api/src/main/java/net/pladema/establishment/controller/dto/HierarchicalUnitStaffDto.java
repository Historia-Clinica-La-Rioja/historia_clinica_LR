package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class HierarchicalUnitStaffDto {
	private Integer id;
	private boolean responsible;
	private Integer hierarchicalUnitId;
	private String hierarchicalUnitAlias;
}

