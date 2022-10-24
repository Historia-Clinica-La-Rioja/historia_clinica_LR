package ar.lamansys.sgx.auth.twoFactorAuthentication.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VerificationCodeDto {

    private String code;

}
