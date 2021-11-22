package net.pladema.clinichistory.external.service;

import net.pladema.clinichistory.external.service.domain.ExternalClinicalHistoryBo;

import java.util.List;

public interface ExternalClinicalHistoryService {

    List<ExternalClinicalHistoryBo> getExternalClinicalHistory(Integer patientId);
}
