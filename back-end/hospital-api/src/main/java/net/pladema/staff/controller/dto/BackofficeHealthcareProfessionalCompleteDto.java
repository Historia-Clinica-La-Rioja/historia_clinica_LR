package net.pladema.staff.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.exceptions.LicenseNumberNullException;
import net.pladema.staff.exceptions.LicenseNumberNullExceptionEnum;

@Getter
@Setter
@ToString
public class BackofficeHealthcareProfessionalCompleteDto {

    private Integer id;

    private Integer personId;

    private String licenseNumber;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;

    private boolean deleted = false;

	public void setLicenseNumber(String licenseNumber) {
		if (!licenseNumber.isBlank())
			this.licenseNumber = licenseNumber;
		else
			throw new LicenseNumberNullException(LicenseNumberNullExceptionEnum.LICENSE_NUMBER_IS_BLANK, "El número de matrícula no puede estar en blanco");
	}
}
