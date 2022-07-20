package net.pladema.staff.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.staff.exceptions.LicenseNumberNullException;
import net.pladema.staff.exceptions.LicenseNumberNullExceptionEnum;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BackofficeHealthcareProfessionalCompleteDto {

    private Integer id;

    private Integer personId;

    private String licenseNumber;

    private Integer professionalSpecialtyId;

    private Integer clinicalSpecialtyId;

    private boolean deleted = false;

	public void setLicenseNumber(String licenseNumber) {
		if (licenseNumber != null && !licenseNumber.isBlank())
			this.licenseNumber = licenseNumber;
		else
			throw new LicenseNumberNullException(LicenseNumberNullExceptionEnum.LICENSE_NUMBER_IS_BLANK,
					"healthcareprofessional.license-blank");
	}
}
