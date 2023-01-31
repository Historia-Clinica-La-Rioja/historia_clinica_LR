package net.pladema.clinichistory.sipplus.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SipPlusUrlDataDto {

	private String token;

	private String urlBase;

}
