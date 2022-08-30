package net.pladema.staff.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.service.domain.ClinicalSpecialtyBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProfessionSpecialtyBo {

	private Integer id;

	private ClinicalSpecialtyBo specialty;

	private List<LicenseNumberBo> licenses;

	public ProfessionSpecialtyBo(Integer id, ClinicalSpecialtyBo specialty, List<LicenseNumberBo> licenses) {
		this.id = id;
		this.licenses = licenses;
		this.specialty = specialty;
	}
}
