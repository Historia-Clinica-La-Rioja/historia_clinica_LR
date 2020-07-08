package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HCEHealthConditionRepository {

    List<HCEHealthConditionVo> getPersonalHistory(Integer patientId);

    List<HCEHealthConditionVo> getFamilyHistory(Integer patientId);
}
