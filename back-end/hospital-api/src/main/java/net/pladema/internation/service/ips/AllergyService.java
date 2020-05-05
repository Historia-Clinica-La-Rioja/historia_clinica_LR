package net.pladema.internation.service.ips;

import net.pladema.internation.service.ips.domain.AllergyConditionBo;

import java.util.List;

public interface AllergyService {

    List<AllergyConditionBo> loadAllergies(Integer patientId, Long id, List<AllergyConditionBo> allergy);

    List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId);
}
