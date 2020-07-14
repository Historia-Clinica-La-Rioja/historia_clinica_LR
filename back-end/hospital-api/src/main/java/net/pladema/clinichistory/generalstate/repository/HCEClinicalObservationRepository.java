package net.pladema.clinichistory.generalstate.repository;

import net.pladema.clinichistory.generalstate.repository.domain.HCEMapClinicalObservationVo;

public interface HCEClinicalObservationRepository {

    HCEMapClinicalObservationVo getGeneralState(Integer patientId);
}
