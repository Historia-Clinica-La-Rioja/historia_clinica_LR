package net.pladema.clinichistory.documents.service.generalstate;

import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;

import java.util.List;

public interface AllergyGeneralStateService {

    List<AllergyConditionBo> getAllergiesGeneralState(Integer internmentEpisodeId);
}
