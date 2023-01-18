package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SipPlusMotherIdentificationDto {

	private String countryCode;

	private String typeCode;

	private String number;

}
