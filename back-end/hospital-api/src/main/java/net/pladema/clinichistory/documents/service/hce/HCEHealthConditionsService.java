package net.pladema.clinichistory.documents.service.hce;

import net.pladema.clinichistory.documents.service.hce.domain.HCEHospitalizationBo;
import net.pladema.clinichistory.documents.service.hce.domain.HCEPersonalHistoryBo;

import java.util.List;

public interface HCEHealthConditionsService {

    List<HCEPersonalHistoryBo> getActivePersonalHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getFamilyHistories(Integer patientId);

    List<HCEPersonalHistoryBo> getChronicConditions(Integer patientId);

    List<HCEPersonalHistoryBo> getActiveProblems(Integer patientId);

    List<HCEPersonalHistoryBo> getSolvedProblems(Integer patientId);

    List<HCEHospitalizationBo> getHospitalizationHistory(Integer patientId);
}
