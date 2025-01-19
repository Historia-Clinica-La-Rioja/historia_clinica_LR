package net.pladema.establishment.sanitaryresponsibilityarea.getinstitutionaddress.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetSanitaryResponsibilityAreaInstitutionAddressDto {

	private MasterDataDto state;

	private MasterDataDto department;

	private MasterDataDto city;

	private String streetName;

	private String houseNumber;

}
