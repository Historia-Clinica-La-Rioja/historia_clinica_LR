package ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto;

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
