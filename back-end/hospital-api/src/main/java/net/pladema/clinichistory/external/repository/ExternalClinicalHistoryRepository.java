package net.pladema.clinichistory.external.repository;

import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistoryVo;

import java.util.List;

public interface ExternalClinicalHistoryRepository {

    List<ExternalClinicalHistoryVo> getAllExternalClinicalHistory(Integer patientId);

}
