package net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.ips.service.domain.*;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class MainDiagnosisBo implements Document {

    private Long id;

    @NotNull
    private boolean confirmed = false;

    @Nullable
    private DocumentObservationsBo notes;

    @Nullable
    private HealthConditionBo mainDiagnosis;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public List<DiagnosisBo> getDiagnosis() {
        return Collections.emptyList();
    }

    @Override
    public List<ProblemBo> getProblems() {
        return Collections.emptyList();
    }

    @Override
    public List<ProcedureBo> getProcedures() {
        return Collections.emptyList();
    }

    @Override
    public List<HealthHistoryConditionBo> getPersonalHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<HealthHistoryConditionBo> getFamilyHistories() {
        return Collections.emptyList();
    }

    @Override
    public List<MedicationBo> getMedications() {
        return Collections.emptyList();
    }

    @Override
    public List<AllergyConditionBo> getAllergies() {
        return Collections.emptyList();
    }

    @Override
    public List<ImmunizationBo> getImmunizations() {
        return Collections.emptyList();
    }

    @Override
    public VitalSignBo getVitalSigns() {
        return null;
    }

    @Override
    public AnthropometricDataBo getAnthropometricData() {
        return null;
    }

    @Override
    public List<ReasonBo> getReasons() {
        return Collections.emptyList();
    }

    @Override
    public ClinicalSpecialtyBo getClinicalSpecialty() {
        return null;
    }
}
