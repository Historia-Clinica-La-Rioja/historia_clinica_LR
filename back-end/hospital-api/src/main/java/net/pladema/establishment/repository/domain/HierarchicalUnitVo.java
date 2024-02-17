package net.pladema.establishment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.HierarchicalUnit;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HierarchicalUnitVo {

	private Integer id;

	private String name;

	private Integer typeId;

	public HierarchicalUnitVo(HierarchicalUnit hierarchicalUnit) {
		this.id = hierarchicalUnit.getId();
		this.typeId = hierarchicalUnit.getTypeId();
		this.name = hierarchicalUnit.getAlias();
	}
}
