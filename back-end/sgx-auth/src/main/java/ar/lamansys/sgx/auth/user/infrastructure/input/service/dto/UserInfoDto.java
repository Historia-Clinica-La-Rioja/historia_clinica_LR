package ar.lamansys.sgx.auth.user.infrastructure.input.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserInfoDto {

    private final Integer id;

    private final String username;

    private final String password;

    private final boolean enabled;

}
