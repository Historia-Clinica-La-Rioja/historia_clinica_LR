package net.pladema.staff.service.domain;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.domain.LicenseNumberBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalSpecialtyBo implements Serializable {

    private Integer id;
    private String name;

	private List<LicenseNumberBo> licenses;
	public ClinicalSpecialtyBo(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
}
