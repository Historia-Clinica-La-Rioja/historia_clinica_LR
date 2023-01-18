package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.sipplus;

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
