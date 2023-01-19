package net.pladema.sisa.refeps.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidatedLicenseNumberBo {

	private String licenseNumber;

	private Boolean isValid;

}
