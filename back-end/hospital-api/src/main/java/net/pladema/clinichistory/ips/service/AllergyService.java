package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.AllergyConditionBo;

import java.util.List;

public interface AllergyService {

    List<AllergyConditionBo> loadAllergies(Integer patientId, Long id, List<AllergyConditionBo> allergy);

    List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId);
}
