package net.pladema.clinichistory.documents.service.ips;

import net.pladema.clinichistory.documents.service.ips.domain.AnthropometricDataBo;
import net.pladema.clinichistory.documents.service.ips.domain.VitalSignBo;

import java.util.Optional;

public interface ClinicalObservationService {

    VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);
}
