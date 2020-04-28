package net.pladema.internation.service.documents.anamnesis;

import net.pladema.internation.service.domain.ips.Medication;

import java.util.List;

public interface MedicationService {

    List<Medication> loadMedications(Integer patientId, Long documentId, List<Medication> medications);

    List<Medication> getMedicationsGeneralState(Integer internmentEpisodeId);
}
