package net.pladema.sisa.refeps.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidatedLicenseDataDto {

	private String licenseNumber;

	private Integer licenseType;

	private Boolean validLicenseNumber;

	private Boolean validLicenseType;

}
