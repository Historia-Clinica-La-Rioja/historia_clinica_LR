package net.pladema.cipres.infrastructure.output.rest.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class CipresLoginPayload {

	private String username;

	private String password;

	private String realusername;

}
