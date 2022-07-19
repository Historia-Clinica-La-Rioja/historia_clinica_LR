package ar.lamansys.sgh.shared.infrastructure.input.service.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSharedInfoDto {

    private Integer id;

	private String username;

	public UserSharedInfoDto(Integer id, String username) {
		this.id = id;
		this.username = username;
	}
}
