package ar.lamansys.sgx.auth.user.infrastructure.input.rest.updateaccessdata;

import ar.lamansys.sgx.auth.user.application.resetpassword.ResetPassword;
import ar.lamansys.sgx.auth.user.application.updateusername.UpdateUsername;
import ar.lamansys.sgx.auth.user.infrastructure.input.rest.updateaccessdata.dto.AccessDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("access-data")
public class AccessDataController {
    private final Logger logger;
    private final ResetPassword resetPassword;
    private final UpdateUsername updateUsername;

    public AccessDataController(ResetPassword resetPassword,
                                UpdateUsername updateUsername) {
        this.resetPassword = resetPassword;
        this.updateUsername = updateUsername;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateAccessData(@NotNull @RequestBody AccessDataDto accessDataDto) {
        logger.debug("Input -> set access data to user {}", accessDataDto.getUsername());
        updateUsername.execute(accessDataDto.getUserId(), accessDataDto.getUsername());
        resetPassword.execute(accessDataDto.getToken(), accessDataDto.getPassword());
    }
}