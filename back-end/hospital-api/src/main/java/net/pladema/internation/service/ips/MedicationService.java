package net.pladema.internation.service.ips;

import net.pladema.internation.service.ips.domain.MedicationBo;

import java.util.List;

public interface MedicationService {

    List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications);

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
