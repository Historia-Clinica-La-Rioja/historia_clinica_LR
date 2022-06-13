package net.pladema.staff.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LicenseNumberBo {

	private Integer id;
	private String number;
	private ELicenseNumberTypeBo type;

}
