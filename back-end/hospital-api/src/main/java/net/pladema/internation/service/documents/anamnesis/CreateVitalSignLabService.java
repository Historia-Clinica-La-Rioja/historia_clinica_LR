package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import net.pladema.internation.service.domain.ips.VitalSignBo;

public interface CreateVitalSignLabService {

    public VitalSignBo loadVitalSigns(Integer patientId, Long documentId, VitalSignBo vitalSigns);

    public AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, AnthropometricDataBo anthropometricData);
}
