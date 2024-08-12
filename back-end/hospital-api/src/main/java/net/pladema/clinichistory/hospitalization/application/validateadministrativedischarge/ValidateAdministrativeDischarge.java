package net.pladema.clinichistory.hospitalization.application.validateadministrativedischarge;

import ar.lamansys.sgh.clinichistory.application.anestheticreport.ports.output.AnestheticReportStorage;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentEnumException;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.InternmentDocumentException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ValidateAdministrativeDischarge {

    private final FeatureFlagsService featureFlagsService;
    private final AnestheticReportStorage anestheticReportStorage;

    public void run(Integer internmentEpisodeId) {
        this.validateAnestheticReportStatus(internmentEpisodeId);
    }

    private void validateAnestheticReportStatus(Integer internmentEpisodeId) {
        if (featureFlagsService.isOn(AppFeature.HABILITAR_PARTE_ANESTESICO_EN_DESARROLLO)) {
            var anestheticReportDraft = anestheticReportStorage.getDocumentIdFromLastAnestheticReportDraft(internmentEpisodeId);
            boolean hasPendingDraft = anestheticReportDraft != null;
            if (hasPendingDraft)
                throw new InternmentDocumentException(InternmentDocumentEnumException.HAS_DOCUMENT_PENDING_DRAFT, "Existe documentación pendiente de completar");
        }
    }
}
