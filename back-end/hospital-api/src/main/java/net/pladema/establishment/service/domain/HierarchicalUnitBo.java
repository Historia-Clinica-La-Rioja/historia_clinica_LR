package net.pladema.establishment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class HierarchicalUnitBo {

	private Integer id;

	private String name;

	private Integer typeId;

	public HierarchicalUnitBo (Integer id, String alias) {
		this.id = id;
		this.name = alias;
	}
}
