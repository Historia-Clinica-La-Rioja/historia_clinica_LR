package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEHospitalizationBo;
import net.pladema.clinichistory.generalstate.service.domain.HCEPersonalHistoryBo;

import java.util.List;

public interface HCEGeneralStateService {

    List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getChronicConditions(Integer patientId);

    List<HCEPersonalHistoryBo> getActiveProblems(Integer patientId);

    List<HCEPersonalHistoryBo> getSolvedProblems(Integer patientId);

    List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId);
}
