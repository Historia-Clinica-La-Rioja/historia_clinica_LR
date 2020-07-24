package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEHealthConditionVo;

import java.util.List;

public interface HCEHealthConditionRepository {

    List<HCEHealthConditionVo> getPersonalHistories(Integer patientId);

    List<HCEHealthConditionVo> getFamilyHistories(Integer patientId);
}
