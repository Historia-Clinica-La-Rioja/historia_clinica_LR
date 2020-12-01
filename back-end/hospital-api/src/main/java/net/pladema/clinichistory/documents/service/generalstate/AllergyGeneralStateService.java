package net.pladema.clinichistory.documents.service.generalstate;

import net.pladema.clinichistory.documents.service.ips.domain.AllergyConditionBo;

import java.util.List;

public interface AllergyGeneralStateService {

    List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId);
}
