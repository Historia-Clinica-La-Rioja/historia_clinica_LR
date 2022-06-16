package net.pladema.staff.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionBo {

	private Integer id;
	private Integer professionId;
	private String description;

	private List<LicenseNumberBo> licenses;

	private List<ProfessionSpecialtyBo> specialties;
	public ProfessionBo(Integer id, Integer professionId, String description) {
		this.id = id;
		this.professionId = professionId;
		this.description = description;
	}

}
