package net.pladema.internation.service.documents;

import net.pladema.internation.service.ips.domain.AllergyConditionBo;
import net.pladema.internation.service.ips.domain.AnthropometricDataBo;
import net.pladema.internation.service.ips.domain.DiagnosisBo;
import net.pladema.internation.service.ips.domain.DocumentObservations;
import net.pladema.internation.service.ips.domain.HealthConditionBo;
import net.pladema.internation.service.ips.domain.HealthHistoryConditionBo;
import net.pladema.internation.service.ips.domain.InmunizationBo;
import net.pladema.internation.service.ips.domain.MedicationBo;
import net.pladema.internation.service.ips.domain.VitalSignBo;

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

    DocumentObservations getNotes();
}
