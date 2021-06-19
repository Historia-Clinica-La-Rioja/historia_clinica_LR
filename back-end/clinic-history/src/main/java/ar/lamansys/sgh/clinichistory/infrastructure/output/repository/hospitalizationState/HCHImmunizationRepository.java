package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ImmunizationVo;

import java.util.List;

public interface HCHImmunizationRepository {

    List<ImmunizationVo> findGeneralState(Integer internmentEpisodeId);
}
