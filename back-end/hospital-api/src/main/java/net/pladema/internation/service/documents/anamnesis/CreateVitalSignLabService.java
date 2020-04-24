package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import net.pladema.internation.service.domain.ips.VitalSignBo;

import java.util.Optional;

public interface CreateVitalSignLabService {

    VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);
}
