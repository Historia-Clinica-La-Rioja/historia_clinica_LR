package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignBo;
import ar.lamansys.sgh.clinichistory.domain.ips.VitalSignObservationBo;

import java.util.Optional;

public interface ClinicalObservationService {

    VitalSignBo loadVitalSigns(PatientInfoBo patientInfo, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(PatientInfoBo patientInfo, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);

    VitalSignObservationBo getObservationById(Integer vitalSignObservationId);
}
