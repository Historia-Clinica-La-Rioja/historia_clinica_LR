package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.documents.repository.hce.domain.HCEHospitalizationVo;

import java.util.List;

public interface HCEHealthConditionRepository {

    List<HCEHealthConditionVo> getPersonalHistories(Integer patientId);

    List<HCEHealthConditionVo> getFamilyHistories(Integer patientId);

    List<HCEHospitalizationVo> getHospitalizationHistory(Integer patientId);
}
