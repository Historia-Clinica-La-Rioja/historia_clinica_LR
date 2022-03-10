package net.pladema.clinichistory.hospitalization.service.impl.exceptions;

import lombok.Getter;

@Getter
public class CreateInternmentEpisodeException extends RuntimeException {

    private final CreateInternmentEpisodeEnumException code;

    public CreateInternmentEpisodeException(CreateInternmentEpisodeEnumException code, String message) {
        super(message);
        this.code = code;
    }
}