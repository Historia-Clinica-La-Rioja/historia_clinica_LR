package net.pladema.emergencycare.triage.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.triage.infrastructure.output.entity.Triage;
import net.pladema.emergencycare.triage.repository.entity.TriageDetails;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class TriageVo {

    private Integer id;

    private Integer emergencyCareEpisodeId;

    private Short emergencyCareTypeId;

    private Short categoryId;

    private Integer doctorsOfficeId;

    private Integer createdBy;

    private String notes;

    private Short bodyTemperatureId;

    private Boolean cryingExcessive;

    private Short muscleHypertoniaId;

    private Short respiratoryRetractionId;

    private Boolean stridor;

    private Short perfusionId;

    private LocalDateTime createdOn;

    private List<Integer> riskFactorIds;

	private Integer clinicalSpecialtySectorId;

	private String clinicalSpecialtySectorDescription;

	private ProfessionalPersonVo creator;

    public TriageVo(Triage triage, TriageDetails triageDetails, Short emergencyCareTypeId, List<Integer> riskFactorIds, String specialtySectorDescription) {
        this.id = triage.getId();
        this.emergencyCareEpisodeId = triage.getEmergencyCareEpisodeId();
        this.emergencyCareTypeId = emergencyCareTypeId;
        this.categoryId = triage.getTriageCategoryId();
        this.createdBy = triage.getCreatedBy();
        this.doctorsOfficeId = triage.getDoctorsOfficeId();
        this.notes = triage.getNotes();
        if (triageDetails != null) {
            this.bodyTemperatureId = triageDetails.getBodyTemperatureId();
            this.cryingExcessive = triageDetails.getCryingExcessive();
            this.muscleHypertoniaId = triageDetails.getMuscleHypertoniaId();
            this.respiratoryRetractionId = triageDetails.getRespiratoryRetractionId();
            this.stridor = triageDetails.getStridor();
            this.perfusionId = triageDetails.getPerfusionId();
        }
        this.createdOn = triage.getCreatedOn();
        this.riskFactorIds = riskFactorIds;
		this.clinicalSpecialtySectorId = triage.getClinicalSpecialtySectorId();
		this.clinicalSpecialtySectorDescription = specialtySectorDescription;
    }

	public TriageVo(Triage triage, Short emergencyCareTypeId, String specialtySectorDescription, ProfessionalPersonVo creator) {
		this.id = triage.getId();
		this.emergencyCareEpisodeId = triage.getEmergencyCareEpisodeId();
		this.emergencyCareTypeId = emergencyCareTypeId;
		this.categoryId = triage.getTriageCategoryId();
		this.createdBy = triage.getCreatedBy();
		this.doctorsOfficeId = triage.getDoctorsOfficeId();
		this.notes = triage.getNotes();
		this.createdOn = triage.getCreatedOn();
		this.clinicalSpecialtySectorId = triage.getClinicalSpecialtySectorId();
		this.clinicalSpecialtySectorDescription = specialtySectorDescription;
		this.creator = creator;
	}
}
