package net.pladema.clinichistory.hospitalization.application.validateanestheticreport;

import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.isadministrativedischarged.IsAdministrativeDischarged;
import net.pladema.clinichistory.hospitalization.domain.HospitalizationDocumentBo;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateHospitalizationAnestheticReport {

    private final IsAdministrativeDischarged isAdministrativeDischarged;

    public Boolean run(HospitalizationDocumentBo hospitalizationDocumentBo) {
        log.debug("Input parameters -> hospitalizationDocumentBo {}", hospitalizationDocumentBo);

        if (hospitalizationDocumentBo.getReason() == null)
            throw new InternmentDocumentException(InternmentDocumentEnumException.NULL_REASON, "Para llevar a cabo la acción el motivo es un campo requerido");

        Integer currentUser = UserInfo.getCurrentAuditor();
        if (!hospitalizationDocumentBo.getCreatedBy().equals(currentUser))
            throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_USER, "El documento únicamente puede ser intervenido por el usuario que lo ha creado");

        Integer internmentEpisodeId = hospitalizationDocumentBo.getSourceId();
        if (isAdministrativeDischarged.run(internmentEpisodeId))
            throw new InternmentDocumentException(InternmentDocumentEnumException.HAVE_ADMINISTRATIVE_DISCHARGE, "No es posible llevar a cabo la acción dado que se ha realizado un alta administrativa");

        if (ChronoUnit.HOURS.between(hospitalizationDocumentBo.getCreatedOn(), LocalDateTime.now()) > 24)
            throw new InternmentDocumentException(InternmentDocumentEnumException.INVALID_DATE, "La acción puede llevarse a cabo únicamente dentro de las 24 hs posteriores a su creación");

        log.debug("Output -> {}", true);
        return true;
    }

}
