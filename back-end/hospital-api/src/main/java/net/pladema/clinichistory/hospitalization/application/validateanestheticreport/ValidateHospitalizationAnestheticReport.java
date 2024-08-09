package net.pladema.clinichistory.hospitalization.application.validateanestheticreport;

import ar.lamansys.sgh.shared.infrastructure.input.service.DocumentReduceInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedDocumentPort;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.isadministrativedischarged.IsAdministrativeDischarged;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateHospitalizationAnestheticReport {

    private final SharedDocumentPort sharedDocumentPort;
    private final IsAdministrativeDischarged isAdministrativeDischarged;

    public void run(Long documentId, String reason) {
        log.debug("Input parameters -> documentId {}", documentId);

        if (reason == null)
            throw new InternmentDocumentException(InternmentDocumentEnumException.NULL_REASON, "Para llevar a cabo la acción el motivo es un campo requerido");

        DocumentReduceInfoDto document = sharedDocumentPort.getDocument(documentId);

        Integer currentUser = UserInfo.getCurrentAuditor();
        if (!document.getCreatedBy().equals(currentUser))
            throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_USER, "El documento únicamente puede ser intervenido por el usuario que lo ha creado");

        Integer internmentEpisodeId = document.getSourceId();
        if (isAdministrativeDischarged.run(internmentEpisodeId))
            throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_ADMINISTRATIVE_DISCHARGE, "No es posible llevar a cabo la acción dado que se ha realizado un alta administrativa");

        if (ChronoUnit.HOURS.between(document.getCreatedOn(), LocalDateTime.now()) > 24)
            throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_DATE, "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación");

        log.debug("Output -> {}", true);
    }

}
