package ar.lamansys.sgh.publicapi.domain.sipplus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusInstitutionBo {

	private SipPlusInstitutionIdBo id;

	private String name;
}
