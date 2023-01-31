package net.pladema.clinichistory.sipplus.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SipPlusUrlDataBo {

	private String token;

	private String urlBase;

}
