package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class EmbeddedAuthenticationDataBo {

	private SipPlusUserBo user;

	private SipPlusInstitutionBo institution;

	private SipPlusCoordinatesBo embedCoordinates;

}
