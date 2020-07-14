package net.pladema.clinichistory.hospitalization.service.generalstate;

import net.pladema.clinichistory.ips.service.domain.AllergyConditionBo;

import java.util.List;

public interface AllergyGeneralStateService {

    List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId);
}
