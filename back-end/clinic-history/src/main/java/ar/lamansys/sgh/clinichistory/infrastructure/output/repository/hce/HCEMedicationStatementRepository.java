package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMedicationVo;

import java.util.List;

public interface HCEMedicationStatementRepository {

    List<HCEMedicationVo> getMedication(Integer patientId);
}
