package net.pladema.clinichistory.hospitalization.service.anamnesis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.hospitalization.service.domain.ClinicalSpecialtyBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProblemBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ReasonBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisBo extends SelfValidating<AnamnesisBo> implements Document {

    private Long id;

    private Integer patientId;

    private Integer encounterId;

    @NotNull(message = "{value.mandatory}")
    private boolean confirmed;

    private DocumentObservationsBo notes;

    private HealthConditionBo mainDiagnosis;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid DiagnosisBo> diagnosis;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid HealthHistoryConditionBo> personalHistories;

    private List<@Valid ProcedureBo> procedures;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid HealthHistoryConditionBo> familyHistories;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid MedicationBo> medications;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid ImmunizationBo> immunizations;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid AllergyConditionBo> allergies;

    @Valid
    private AnthropometricDataBo anthropometricData;

    @Valid
    private VitalSignBo vitalSigns;

    public String getDocumentStatusId(){
        return confirmed ? DocumentStatus.FINAL : DocumentStatus.DRAFT;
    }

    public List<DiagnosisBo> getAlternativeDiagnosis() {
        return diagnosis;
    }

    @Override
    public List<ProblemBo> getProblems() {
        return Collections.emptyList();
    }

    @Override
    public List<ReasonBo> getReasons() {
        return Collections.emptyList();
    }

    @Override
    public ClinicalSpecialtyBo getClinicalSpecialty() {
        return null;
    }
    
    public short getDocumentType() {
        return DocumentType.ANAMNESIS;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }
}
