package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEMedicationBo;

import java.util.List;

public interface HCEMedicationService {

    List<HCEMedicationBo> getMedication(Integer patientId);
}
