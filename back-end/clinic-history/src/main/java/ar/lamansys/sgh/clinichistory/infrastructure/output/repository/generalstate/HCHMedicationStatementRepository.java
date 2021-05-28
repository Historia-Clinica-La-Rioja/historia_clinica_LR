package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.generalstate.entity.MedicationVo;

import java.util.List;

public interface HCHMedicationStatementRepository {

    List<MedicationVo> findGeneralState(Integer internmentEpisodeId);
}
