package net.pladema.clinichistory.hospitalization.service.epicrisis.domain;

import ar.lamansys.sgh.clinichistory.domain.ReferableItemBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AllergyConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.AnthropometricDataBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosisBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ExternalCauseBo;
import ar.lamansys.sgh.clinichistory.domain.ips.FamilyHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ImmunizationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.MedicationBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ObstetricEventBo;
import ar.lamansys.sgh.clinichistory.domain.ips.PersonalHistoryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProcedureBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

	private LocalDate patientInternmentAge;

    @NotNull(message = "{value.mandatory}")
    private HealthConditionBo mainDiagnosis;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid DiagnosisBo> diagnosis;

    @NotNull(message = "{value.mandatory}")
    private ReferableItemBo<@Valid PersonalHistoryBo> personalHistories;

	@NotNull(message = "{value.mandatory}")
	private List<@Valid ProcedureBo> procedures;

    @NotNull(message = "{value.mandatory}")
    private ReferableItemBo<@Valid FamilyHistoryBo> familyHistories;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid MedicationBo> medications;

    @NotNull(message = "{value.mandatory}")
    private List<@Valid ImmunizationBo> immunizations;

    @NotNull(message = "{value.mandatory}")
    private ReferableItemBo<@Valid AllergyConditionBo> allergies;

	private List<@Valid HealthConditionBo> otherProblems;

    @Valid
    private AnthropometricDataBo anthropometricData;

    @Valid
    private RiskFactorBo riskFactors;

	private ExternalCauseBo externalCause;

	private ObstetricEventBo obstetricEvent;

    private LocalDateTime performedDate;

	private Long initialDocumentId;

	private String modificationReason;
	
	private boolean confirmed;

	private Integer roomId;

	private Integer sectorId;

	private Integer medicalCoverageId;

    private Map<String, Object> contextMap;

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

	public Long getPreviousDocumentId () { return getInitialDocumentId() == null ? getId() : getInitialDocumentId(); }

}
