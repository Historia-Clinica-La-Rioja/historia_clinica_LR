package net.pladema.security.authentication.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OauthConfigDto {

	private String loginUrl;

	private Boolean enabled;

}
