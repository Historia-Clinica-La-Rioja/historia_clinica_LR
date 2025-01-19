package net.pladema.clinichistory.hospitalization.application.setpatientfrominternmenteposiode;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.PatientNotFoundException;
import net.pladema.patient.controller.service.PatientExternalService;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SetPatientFromInternmentEpisode {

    private final InternmentEpisodeService internmentEpisodeService;
    private final PatientExternalService patientExternalService;

    public Boolean run(IDocumentBo documentBo) {
        log.debug("Input parameters -> documentBo {}", documentBo);

        internmentEpisodeService.getPatient(documentBo.getEncounterId())
                .map(patientExternalService::getBasicDataFromPatient)
                .map(patientDto -> new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge()))
                .ifPresentOrElse(patientInfo -> {
                    documentBo.setPatientInfo(patientInfo);
                    documentBo.setPatientId(patientInfo.getId());
                }, PatientNotFoundException::new);

        log.debug("Output -> patientInfoBo {} from document id {}", documentBo.getPatientInfo(), documentBo.getId());
        return true;
    }
}
