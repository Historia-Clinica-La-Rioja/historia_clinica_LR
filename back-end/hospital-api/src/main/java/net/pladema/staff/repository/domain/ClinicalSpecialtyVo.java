package net.pladema.staff.repository.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalSpecialtyVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3858705190577876194L;

	private Integer id;

	private String name;

	public ClinicalSpecialtyVo(ClinicalSpecialty clinicalSpecialty) {
		this.id = clinicalSpecialty.getId();
		this.name = clinicalSpecialty.getName();
	}

}
