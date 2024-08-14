package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.domain.SectorVo;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SectorBo implements Serializable {

	private Integer id;

	private String description;

	private Short type;

	public SectorBo(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public SectorBo(SectorVo vo){
		this.id = vo.getId();
		this.description = vo.getDescription();
		this.type = vo.getType();

	}
	
}