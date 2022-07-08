package ar.lamansys.sgh.shared.infrastructure.input.service.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@NoArgsConstructor
public class UserSharedInfoDto {

    private Integer id;

	public UserSharedInfoDto(Integer id) {
		this.id = id;
	}
}
