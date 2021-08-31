package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;

public interface HCEClinicalObservationRepository {

    HCEMapClinicalObservationVo getGeneralState(Integer patientId);
}
