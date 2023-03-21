package net.pladema.user.controller.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminUserDto {
	private Integer id;
	private Integer personId;
	private String username;
	private Boolean enable;
	private LocalDateTime lastLogin;
	private Boolean twoFactorAuthenticationEnabled;

	
}
