package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;
import net.pladema.clinichistory.generalstate.repository.domain.HCEHospitalizationVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HCEHealthConditionRepository {

    List<HCEHealthConditionVo> getPersonalHistories(Integer patientId);

    List<HCEHealthConditionVo> getFamilyHistories(Integer patientId);

    List<HCEHospitalizationVo> getHospitalizationHistory(Integer patientId);
}
