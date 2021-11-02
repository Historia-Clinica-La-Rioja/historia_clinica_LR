package ar.lamansys.sgx.auth.user.domain.passwordreset;

import lombok.AllArgsConstructor;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PasswordResetTokenBo {

    private Long id;

    private String token;

    private Integer userId;

    private Boolean enable ;

    private LocalDateTime expiryDate;
}
