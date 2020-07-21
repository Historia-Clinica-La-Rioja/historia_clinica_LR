package net.pladema.clinichistory.documents.service;

import net.pladema.clinichistory.ips.service.domain.*;
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

    List<InmunizationBo> getInmunizations();

    VitalSignBo getVitalSigns();

    AnthropometricDataBo getAnthropometricData();

    DocumentObservationsBo getNotes();

    List<ReasonBo> getReasons();
}
