package ar.lamansys.sgh.publicapi.sipplus.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class EmbeddedAuthenticationDataDto {

	private SipPlusUserDto user;

	private SipPlusInstitutionDto institution;

	private SipPlusCoordinatesDto embedCoordinates;
}
