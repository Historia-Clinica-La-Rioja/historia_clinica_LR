package net.pladema.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BackofficeUserDto {
	private Integer id;
	private Integer personId;
	private String username;
	private Boolean enable;
	private LocalDateTime lastLogin;
}
