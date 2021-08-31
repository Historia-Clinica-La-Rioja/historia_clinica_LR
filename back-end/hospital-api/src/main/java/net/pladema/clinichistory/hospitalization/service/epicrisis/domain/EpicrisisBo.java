package net.pladema.clinichistory.hospitalization.service.epicrisis.domain;

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
import java.util.List;

@Getter
@Setter
@ToString
public class EpicrisisBo extends SelfValidating<EpicrisisBo> implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    private DocumentObservationsBo notes;

    @NotNull(message = "{value.mandatory}")
    private HealthConditionBo mainDiagnosis;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid DiagnosisBo> diagnosis;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid HealthHistoryConditionBo> personalHistories;

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

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    public short getDocumentType() {
        return DocumentType.EPICRISIS;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }
}
