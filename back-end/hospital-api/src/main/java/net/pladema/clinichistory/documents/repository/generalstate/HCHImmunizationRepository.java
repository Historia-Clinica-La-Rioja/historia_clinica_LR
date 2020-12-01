package net.pladema.clinichistory.documents.repository.generalstate;

import net.pladema.clinichistory.documents.repository.generalstate.domain.ImmunizationVo;

import java.util.List;

public interface HCHImmunizationRepository {

    List<ImmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
