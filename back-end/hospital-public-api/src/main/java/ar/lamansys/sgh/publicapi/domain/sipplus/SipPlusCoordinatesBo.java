package ar.lamansys.sgh.publicapi.domain.sipplus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusCoordinatesBo {

	private String form;

	private String embedId;

	private SipPlusMotherIdentificationBo motherIdentification;

	private Boolean ignoreLocks;

	private Integer pregnancy;
}
