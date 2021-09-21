package net.pladema.clinichistory.external.repository;

import net.pladema.clinichistory.external.repository.domain.ExternalClinicalHistorySummaryVo;

import java.util.List;

public interface ExternalClinicalHistorySummaryRepository {

    List<ExternalClinicalHistorySummaryVo> getAllExternalClinicalHistorySummary(Integer patientId);

}
