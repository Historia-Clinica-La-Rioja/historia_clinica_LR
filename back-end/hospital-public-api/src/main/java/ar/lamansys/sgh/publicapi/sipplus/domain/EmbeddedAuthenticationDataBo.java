package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class EmbeddedAuthenticationDataBo {

	private SipPlusUserBo user;

	private SipPlusInstitutionBo institution;

	private SipPlusCoordinatesBo embedCoordinates;

}
