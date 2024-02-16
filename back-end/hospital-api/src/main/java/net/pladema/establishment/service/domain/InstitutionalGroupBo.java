package net.pladema.establishment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.establishment.repository.entity.InstitutionalGroup;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionalGroupBo {

	private Integer id;
	private Short typeId;
	private String name;

	public InstitutionalGroupBo(InstitutionalGroup entity){
		this.id = entity.getId();
		this.typeId = entity.getTypeId();
		this.name = entity.getName();
	}

}
