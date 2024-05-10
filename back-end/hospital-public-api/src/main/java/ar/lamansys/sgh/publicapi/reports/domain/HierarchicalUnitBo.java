package ar.lamansys.sgh.publicapi.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HierarchicalUnitBo {
	private Integer id;
	private String description;
	private Short type;
}
