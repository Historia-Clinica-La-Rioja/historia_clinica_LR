package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.AnthropometricDataBo;
import net.pladema.internation.service.domain.ips.VitalSignBo;

import java.util.List;

public interface VitalSignLabService {

    List<VitalSignBo> loadVitalSigns(Integer patientId, Long documentId, List<VitalSignBo> optVitalSigns);

    List<AnthropometricDataBo> loadAnthropometricData(Integer patientId, Long documentId, List<AnthropometricDataBo> optAnthropometricData);

    List<AnthropometricDataBo> getAnthropometricDataGeneralState(Integer internmentEpisodeId);

    List<VitalSignBo> getLast2VitalSignsGeneralState(Integer internmentEpisodeId);
}
