package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.*;
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

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class EvolutionNoteBo extends SelfValidating<EvolutionNoteBo> implements IDocumentBo {

    private Long id;

    private Integer patientId;

    private PatientInfoBo patientInfo;

    private Integer encounterId;

    private Integer institutionId;

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
    private RiskFactorBo riskFactors;

    private LocalDateTime performedDate;

    private Boolean wasMadeByProfessionalNursing;

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    @Override
    public short getDocumentType() {
    	if (this.wasMadeByProfessionalNursing)
    		return DocumentType.NURSING_EVOLUTION_NOTE;
    	return DocumentType.EVALUATION_NOTE;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }

}
