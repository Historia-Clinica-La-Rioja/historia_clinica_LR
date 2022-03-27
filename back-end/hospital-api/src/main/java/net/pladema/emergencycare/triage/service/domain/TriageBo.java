package net.pladema.emergencycare.triage.service.domain;

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
public class TriageBo {

    private Integer id;

    private Integer emergencyCareEpisodeId;

    private Short emergencyCareTypeId;

    private Short categoryId;

    private Integer doctorsOfficeId;

    private Integer createdBy;

    private String notes;

    private Short bodyTemperatureId;

    private String bodyTemperatureDescription;

    private Boolean cryingExcessive;

    private Short muscleHypertoniaId;

    private String muscleHypertoniaDescription;

    private Short respiratoryRetractionId;

    private String respiratoryRetractionDescription;

    private Boolean stridor;

    private Short perfusionId;

    private String perfusionDescription;

    private LocalDateTime createdOn;

    private List<Integer> riskFactorIds;

    public TriageBo(TriageVo triageVo) {
        this.id = triageVo.getId();
        this.emergencyCareEpisodeId = triageVo.getEmergencyCareEpisodeId();
        this.emergencyCareTypeId = triageVo.getEmergencyCareTypeId();
        this.categoryId = triageVo.getCategoryId();
        this.doctorsOfficeId = triageVo.getDoctorsOfficeId();
        this.createdBy = triageVo.getCreatedBy();
        this.notes = triageVo.getNotes();
        this.bodyTemperatureId = triageVo.getBodyTemperatureId();
        this.cryingExcessive = triageVo.getCryingExcessive();
        this.muscleHypertoniaId = triageVo.getMuscleHypertoniaId();
        this.respiratoryRetractionId = triageVo.getRespiratoryRetractionId();
        this.stridor = triageVo.getStridor();
        this.perfusionId = triageVo.getPerfusionId();
        this.createdOn = triageVo.getCreatedOn();
        this.riskFactorIds = triageVo.getRiskFactorIds();
    }

    public boolean isAdultGynecological() {
        return EEmergencyCareType.ADULTO.getId().equals(emergencyCareTypeId) ||
                EEmergencyCareType.GINECOLOGICA.getId().equals(emergencyCareTypeId);
    }

    public boolean isPediatric() {
        return EEmergencyCareType.PEDIATRIA.getId().equals(emergencyCareTypeId);
    }

}
