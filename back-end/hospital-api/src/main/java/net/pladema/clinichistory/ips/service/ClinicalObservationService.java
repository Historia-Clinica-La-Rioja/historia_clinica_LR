package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.AnthropometricDataBo;
import net.pladema.clinichistory.ips.service.domain.VitalSignBo;

import java.util.Optional;

public interface ClinicalObservationService {

    VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);
}
