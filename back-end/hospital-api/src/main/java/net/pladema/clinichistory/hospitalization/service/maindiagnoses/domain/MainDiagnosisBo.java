package net.pladema.clinichistory.hospitalization.service.maindiagnoses.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgx.shared.exceptions.SelfValidating;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
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

	private LocalDateTime performedDate;

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
