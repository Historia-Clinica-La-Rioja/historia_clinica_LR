package net.pladema.establishment.repository.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.Sector;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SectorVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9168875811628523933L;
	
	private Integer id;

	private String description;
	
	public SectorVo(Sector sector) {
		id = sector.getId();
		description = sector.getDescription();
	}
	
}
