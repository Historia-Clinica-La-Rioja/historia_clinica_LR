package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.ImmunizationVo;

import java.util.List;

public interface HCHImmunizationRepository {

    List<ImmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
