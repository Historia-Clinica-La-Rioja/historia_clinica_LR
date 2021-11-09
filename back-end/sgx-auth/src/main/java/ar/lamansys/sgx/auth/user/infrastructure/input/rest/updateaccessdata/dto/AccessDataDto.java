package ar.lamansys.sgx.auth.user.infrastructure.input.rest.updateaccessdata.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccessDataDto {
    @NotNull
    private Integer userId;
    @NotNull
    private String username;
    @NotNull(message = "{token.mandatory}")
    @NotBlank(message = "{token.mandatory}")
    private String token;
    @NotNull(message = "{password.mandatory}")
    @NotBlank(message = "{password.mandatory}")
    private String password;
}