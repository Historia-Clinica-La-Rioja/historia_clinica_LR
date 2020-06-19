package net.pladema.clinichistory.ips.service;

import net.pladema.clinichistory.ips.service.domain.MedicationBo;

import java.util.List;

public interface MedicationService {

    List<MedicationBo> loadMedications(Integer patientId, Long documentId, List<MedicationBo> medications);

    List<MedicationBo> getMedicationsGeneralState(Integer internmentEpisodeId);
}
