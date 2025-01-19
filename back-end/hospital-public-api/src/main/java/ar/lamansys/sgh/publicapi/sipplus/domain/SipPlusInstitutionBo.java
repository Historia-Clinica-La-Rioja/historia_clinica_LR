package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class SipPlusInstitutionBo {

	private SipPlusInstitutionIdBo id;

	private String name;
}
