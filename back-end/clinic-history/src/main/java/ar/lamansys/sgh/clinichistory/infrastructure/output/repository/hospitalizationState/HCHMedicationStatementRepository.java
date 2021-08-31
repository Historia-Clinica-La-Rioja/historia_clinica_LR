package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.MedicationVo;

import java.util.List;

public interface HCHMedicationStatementRepository {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
