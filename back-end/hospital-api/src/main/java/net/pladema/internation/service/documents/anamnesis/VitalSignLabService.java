package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import net.pladema.internation.service.domain.ips.VitalSignBo;

import java.util.List;
import java.util.Optional;

public interface VitalSignLabService {

    VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);

    List<AnthropometricDataBo> getAnthropometricDataGeneralState(Integer internmentEpisodeId);

    List<VitalSignBo> getLast2VitalSignsGeneralState(Integer internmentEpisodeId);
}
