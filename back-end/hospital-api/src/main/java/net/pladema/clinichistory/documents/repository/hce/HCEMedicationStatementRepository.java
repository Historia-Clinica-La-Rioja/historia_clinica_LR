package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEMedicationVo;

import java.util.List;

public interface HCEMedicationStatementRepository {

    List<HCEMedicationVo> getMedication(Integer patientId);
}
