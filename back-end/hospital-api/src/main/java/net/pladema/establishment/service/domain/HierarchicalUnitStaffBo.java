package net.pladema.establishment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HierarchicalUnitStaffBo {
	private Integer id;
	private boolean responsible;
	private Integer hierarchicalUnitId;
	private String hierarchicalUnitAlias;
}

