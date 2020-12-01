package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;

import java.util.List;

public interface Document {

    Long getId();

    boolean isConfirmed();

    HealthConditionBo getMainDiagnosis();

    List<DiagnosisBo> getDiagnosis();

    List<ProblemBo> getProblems();

    List<ProcedureBo> getProcedures();

    List<HealthHistoryConditionBo> getPersonalHistories();

    List<HealthHistoryConditionBo> getFamilyHistories();

    List<MedicationBo> getMedications();

    List<AllergyConditionBo> getAllergies();

    List<ImmunizationBo> getImmunizations();

    VitalSignBo getVitalSigns();

    AnthropometricDataBo getAnthropometricData();

    DocumentObservationsBo getNotes();

    List<ReasonBo> getReasons();

    ClinicalSpecialtyBo getClinicalSpecialty();
}
