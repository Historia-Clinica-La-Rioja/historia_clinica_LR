package net.pladema.emergencycare.triage.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.repository.entity.Triage;
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

    public TriageVo(Triage triage, TriageDetails triageDetails, Short emergencyCareTypeId, List<Integer> riskFactorIds) {
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
    }

}
