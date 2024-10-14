package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusInstitutionIdBo {

	private String countryId;

	private String divisionId;

	private String subdivisionId;

	private String code;
}
