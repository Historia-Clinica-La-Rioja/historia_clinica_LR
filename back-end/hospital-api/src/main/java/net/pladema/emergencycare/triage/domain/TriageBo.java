package net.pladema.emergencycare.triage.domain;

import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DocumentObservationsBo;
import ar.lamansys.sgh.clinichistory.domain.ips.OtherRiskFactorBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import ar.lamansys.sgh.clinichistory.domain.ips.RiskFactorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.service.domain.enums.EEmergencyCareType;
import net.pladema.emergencycare.triage.repository.domain.TriageVo;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TriageBo implements IDocumentBo {

	private Long id;

    private Integer triageId;

    private Integer emergencyCareEpisodeId;

	private Integer encounterId;

	private Integer institutionId;

	private PatientInfoBo patientInfo;

	private LocalDateTime performedDate = LocalDateTime.now();

	private RiskFactorBo riskFactors;

    private Short emergencyCareTypeId;

    private Short categoryId;

    private Integer doctorsOfficeId;

    private Integer createdBy;

    private String notes;

	private OtherRiskFactorBo otherRiskFactors;

    private LocalDateTime createdOn;

    private List<Integer> riskFactorIds;

	private Integer medicalCoverageId;

	private Integer shockRoomId;

	private Integer roomId;

	private Integer sectorId;
	private List<ReasonBo> reasons;

    public TriageBo(TriageVo triageVo) {
        this.triageId = triageVo.getId();
        this.emergencyCareEpisodeId = triageVo.getEmergencyCareEpisodeId();
        this.emergencyCareTypeId = triageVo.getEmergencyCareTypeId();
        this.categoryId = triageVo.getCategoryId();
        this.doctorsOfficeId = triageVo.getDoctorsOfficeId();
        this.createdBy = triageVo.getCreatedBy();
        this.notes = triageVo.getNotes();
		this.otherRiskFactors = new OtherRiskFactorBo(triageVo.getBodyTemperatureId(), triageVo.getCryingExcessive(), triageVo.getMuscleHypertoniaId(),
				triageVo.getRespiratoryRetractionId(), triageVo.getStridor(), triageVo.getPerfusionId());
        this.createdOn = triageVo.getCreatedOn();
        this.riskFactorIds = triageVo.getRiskFactorIds();
		this.encounterId = triageVo.getEmergencyCareEpisodeId();
    }

    public boolean isAdultGynecological() {
        return EEmergencyCareType.ADULTO.getId().equals(emergencyCareTypeId) ||
                EEmergencyCareType.GINECOLOGICA.getId().equals(emergencyCareTypeId);
    }

    public boolean isPediatric() {
        return EEmergencyCareType.PEDIATRIA.getId().equals(emergencyCareTypeId);
    }

	@Override
	public short getDocumentType() {
		return DocumentType.TRIAGE;
	}

	@Override
	public Short getDocumentSource() {
		return SourceType.EMERGENCY_CARE;
	}

	@Override
	public Integer getPatientId() {
		return patientInfo.getId();
	}

	@Override
	public DocumentObservationsBo getNotes() {
		if (notes != null) {
			DocumentObservationsBo result = new DocumentObservationsBo();
			result.setOtherNote(notes);
			return result;
		}
		return null;
	}

	@Override
	public List<ReasonBo> getReasons() {
		return reasons;
	}

}
