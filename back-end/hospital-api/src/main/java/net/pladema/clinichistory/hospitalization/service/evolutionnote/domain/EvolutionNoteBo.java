package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.Document;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentStatus;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.hospitalization.service.anamnesis.domain.AnamnesisBo;
import net.pladema.clinichistory.outpatient.createoutpatient.service.domain.ProcedureBo;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNoteBo extends SelfValidating<EvolutionNoteBo> implements Document {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    @NotNull(message = "{value.mandatory}")
    private boolean confirmed;

    private DocumentObservationsBo notes;

    @Nullable
    private HealthConditionBo mainDiagnosis;

    @Nullable
    private List<@Valid DiagnosisBo> diagnosis;

    @Nullable
    private List<@Valid ImmunizationBo> immunizations;

    @Nullable
    private List<@Valid AllergyConditionBo> allergies;

    @Nullable
    private List<@Valid ProcedureBo> procedures;

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

    @Override
    public short getDocumentType() {
        return DocumentType.EVALUATION_NOTE;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }

}
