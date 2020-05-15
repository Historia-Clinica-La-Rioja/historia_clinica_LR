package net.pladema.internation.service.ips;

import net.pladema.internation.service.internment.domain.Last2VitalSignsBo;
import net.pladema.internation.service.ips.domain.AnthropometricDataBo;
import net.pladema.internation.service.ips.domain.VitalSignBo;

import java.util.Optional;

public interface ClinicalObservationService {

    VitalSignBo loadVitalSigns(Integer patientId, Long documentId, Optional<VitalSignBo> optVitalSigns);

    AnthropometricDataBo loadAnthropometricData(Integer patientId, Long documentId, Optional<AnthropometricDataBo> optAnthropometricData);

    AnthropometricDataBo getLastAnthropometricDataGeneralState(Integer internmentEpisodeId);

    Last2VitalSignsBo getLast2VitalSignsGeneralState(Integer internmentEpisodeId);
}
