package net.pladema.clinichistory.documents.repository.hce;

import net.pladema.clinichistory.documents.repository.hce.domain.HCEMapClinicalObservationVo;

public interface HCEClinicalObservationRepository {

    HCEMapClinicalObservationVo getGeneralState(Integer patientId);
}
