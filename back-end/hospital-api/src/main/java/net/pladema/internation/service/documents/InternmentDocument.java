package net.pladema.internation.service.documents;

import net.pladema.internation.service.ips.domain.*;

import java.util.List;

public interface InternmentDocument {

    Long getId();

    boolean isConfirmed();

    HealthConditionBo getMainDiagnosis();

    List<DiagnosisBo> getDiagnosis();

    List<HealthHistoryConditionBo> getPersonalHistories();

    List<HealthHistoryConditionBo> getFamilyHistories();

    List<MedicationBo> getMedications();

    List<AllergyConditionBo> getAllergies();

    List<InmunizationBo> getInmunizations();

    VitalSignBo getVitalSigns();

    AnthropometricDataBo getAnthropometricData();

    DocumentObservationsBo getNotes();
}
