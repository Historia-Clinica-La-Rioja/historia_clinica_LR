package net.pladema.sisa.refeps.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ValidatedLicenseNumberDto {

	private String licenseNumber;

	private Boolean isValid;

}
