package net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.DocumentType;
import net.pladema.clinichistory.documents.service.IDocumentBo;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.documents.service.ips.domain.*;
import net.pladema.clinichistory.outpatient.repository.domain.SourceType;
import net.pladema.sgx.exceptions.SelfValidating;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class MainDiagnosisBo extends SelfValidating<MainDiagnosisBo> implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

    @NotNull
    private boolean confirmed = false;

    @NotNull
    private DocumentObservationsBo notes;

    @NotNull(message = "{diagnosis.mandatory}")
    private HealthConditionBo mainDiagnosis;

    @Nullable
    private List<@Valid DiagnosisBo> diagnosis;

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
