package net.pladema.clinichistory.hospitalization.repository.generalstate;

import net.pladema.clinichistory.hospitalization.repository.generalstate.domain.InmunizationVo;

import java.util.List;

public interface HCHInmunizationRepository {

    List<InmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
