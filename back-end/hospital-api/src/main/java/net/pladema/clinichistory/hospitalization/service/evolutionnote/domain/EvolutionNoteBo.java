package net.pladema.clinichistory.hospitalization.service.evolutionnote.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
import javax.validation.Valid;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

	private LocalDate patientInternmentAge;

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

    private Boolean isNursingEvolutionNote;

	private String modificationReason;

	private Long initialDocumentId;

    @Override
    public Integer getPatientId() {
        if (patientInfo != null)
            return patientInfo.getId();
        return patientId;
    }

    @Override
    public short getDocumentType() {
    	if (this.isNursingEvolutionNote)
    		return DocumentType.NURSING_EVOLUTION_NOTE;
    	return DocumentType.EVALUATION_NOTE;
    }

    @Override
    public Short getDocumentSource() {
        return SourceType.HOSPITALIZATION;
    }

}
