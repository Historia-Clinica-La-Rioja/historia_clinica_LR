package net.pladema.clinichistory.hospitalization.controller.exceptions;

import ar.lamansys.sgx.auth.user.application.registeruser.exceptions.RegisterUserException;
import ar.lamansys.sgx.auth.user.domain.passwordreset.exceptions.PasswordResetTokenStorageException;
import ar.lamansys.sgx.auth.user.domain.user.model.UserException;
import ar.lamansys.sgx.auth.user.domain.user.service.exceptions.UserStorageException;
import ar.lamansys.sgx.auth.user.domain.userpassword.UserPasswordException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.CreateInternmentEpisodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"net.pladema.clinichistory.hospitalization"})
public class HospitalizationExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(HospitalizationExceptionHandler.class);

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({CreateInternmentEpisodeException.class})
    protected ApiErrorMessageDto handleCreateInternmentEpisodeException(CreateInternmentEpisodeException ex) {
        LOG.debug("CreateInternmentEpisodeException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

}

