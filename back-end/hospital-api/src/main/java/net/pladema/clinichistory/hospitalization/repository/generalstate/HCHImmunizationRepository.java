package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.ImmunizationVo;

import java.util.List;

public interface HCHImmunizationRepository {

    List<ImmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
