package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapClinicalObservationVo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hce.entity.HCEMapHistoricClinicalObservationVo;

import java.util.List;

public interface HCEClinicalObservationRepository {

    HCEMapClinicalObservationVo getGeneralState(Integer patientId, List<Short> invalidDocumentTypes);

	HCEMapHistoricClinicalObservationVo getHistoricData(Integer patientId, List<Short> invalidDocumentTypes);

}
