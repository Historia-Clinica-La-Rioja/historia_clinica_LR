package ar.lamansys.sgh.publicapi.sipplus.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusMotherIdentificationBo {

	private String countryCode;

	private String typeCode;

	private String number;
}
