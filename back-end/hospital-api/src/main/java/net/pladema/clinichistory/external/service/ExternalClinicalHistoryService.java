package net.pladema.clinichistory.external.service;

import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistorySummaryBo;
import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;

import java.util.List;

public interface ExternalClinicalHistoryService {

    List<ExternalClinicalHistorySummaryBo> getExternalClinicalHistory(Integer patientId);

    Integer save(ExternalClinicalHistoryBo externalClinicHistoryDto);
}
