package net.pladema.clinichistory.hospitalization.service.epicrisis.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class EpicrisisBo extends SelfValidating<EpicrisisBo> implements Document {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    @NotNull(message = "{value.mandatory}")
    private boolean confirmed;

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
