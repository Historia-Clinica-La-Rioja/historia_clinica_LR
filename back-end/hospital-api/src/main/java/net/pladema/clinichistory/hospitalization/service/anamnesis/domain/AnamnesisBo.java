package net.pladema.clinichistory.hospitalization.service.anamnesis.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgx.shared.exceptions.SelfValidating;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class AnamnesisBo extends SelfValidating<AnamnesisBo> implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

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

    private LocalDateTime performedDate;

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    public List<DiagnosisBo> getAlternativeDiagnosis() {
        return diagnosis;
    }

    public short getDocumentType() {
        return DocumentType.ANAMNESIS;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }
}
