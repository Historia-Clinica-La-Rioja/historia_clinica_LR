package net.pladema.clinichistory.generalstate.service;

import net.pladema.clinichistory.generalstate.service.domain.HCEMedicationBo;

import java.util.List;

public interface HCEMedicationService {

    List<HCEMedicationBo> getMedication(Integer patientId);
}
