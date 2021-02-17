package net.pladema.emergencycare.service;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.emergencycare.service.domain.MedicalDischargeBo;

public interface EmergencyCareEpisodeDischargeService {

    boolean newMedicalDischarge(MedicalDischargeBo medicalDischargeBo);


}
