package ar.lamansys.sgx.auth.jwt.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoFactorAuthenticationLoginDto {

    private String code;

}
