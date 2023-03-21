package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

import ar.lamansys.sgh.publicapi.domain.sipplus.SipPlusInstitutionBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class SipPlusInstitutionDto {

	private SipPlusInstitutionIdDto id;

	private String name;

	public SipPlusInstitutionDto (SipPlusInstitutionBo sipPlusInstitutionBo) {
		SipPlusInstitutionIdDto sipPlusInstitutionIdDto = SipPlusInstitutionIdDto.builder()
				.code(sipPlusInstitutionBo.getId().getCode())
				.countryId(sipPlusInstitutionBo.getId().getCountryId())
				.divisionId(sipPlusInstitutionBo.getId().getDivisionId())
				.subdivisionId(sipPlusInstitutionBo.getId().getSubdivisionId())
				.build();
		this.id = sipPlusInstitutionIdDto;
		this.name = sipPlusInstitutionBo.getName();
	}

}
