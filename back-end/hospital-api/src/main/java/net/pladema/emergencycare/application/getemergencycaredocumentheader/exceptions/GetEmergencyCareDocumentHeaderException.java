package net.pladema.emergencycare.application.getemergencycaredocumentheader.exceptions;

import lombok.Getter;
import net.pladema.emergencycare.domain.exceptions.GetEmergencyCareDocumentHeaderEnumException;

@Getter

public class GetEmergencyCareDocumentHeaderException extends RuntimeException {

    private final GetEmergencyCareDocumentHeaderEnumException code;

    public GetEmergencyCareDocumentHeaderException(GetEmergencyCareDocumentHeaderEnumException code, String message) {
        super(message);
        this.code = code;
    }
}
