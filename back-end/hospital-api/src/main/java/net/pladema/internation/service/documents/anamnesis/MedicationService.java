package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.MedicationBo;

import java.util.List;

public interface MedicationService {

    List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications);

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
