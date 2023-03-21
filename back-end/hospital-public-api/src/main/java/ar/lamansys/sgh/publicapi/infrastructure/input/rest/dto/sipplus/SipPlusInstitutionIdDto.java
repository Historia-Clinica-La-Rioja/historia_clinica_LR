package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusInstitutionIdDto {

	private String countryId;

	private String divisionId;

	private String subdivisionId;

	private String code;

}
