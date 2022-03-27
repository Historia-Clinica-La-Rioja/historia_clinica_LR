package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;

import java.util.List;

public interface HCEClinicalObservationRepository {

    HCEMapClinicalObservationVo getGeneralState(Integer patientId, List<Short> invalidDocumentTypes);

}
