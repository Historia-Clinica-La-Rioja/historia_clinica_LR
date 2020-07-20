package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEMedicationVo;

import java.util.List;

public interface HCEMedicationStatementRepository {

    List<HCEMedicationVo> getMedication(Integer patientId);
}
