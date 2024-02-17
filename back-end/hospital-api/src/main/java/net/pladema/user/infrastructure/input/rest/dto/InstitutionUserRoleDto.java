package net.pladema.user.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InstitutionUserRoleDto {
	private List<Short> rolesId;
}
