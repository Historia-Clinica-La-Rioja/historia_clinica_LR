package net.pladema.emergencycare.triage.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private Short categoryId;

    private Integer doctorsOfficeId;

    private String notes;

    private Short bodyTemperatureId;

    private Boolean cryingExcessive;

    private Short muscleHypertoniaId;

    private Short respiratoryRetractionId;

    private Boolean stridor;

    private Short perfusionId;

    private List<Integer> vitalSignIds;

}
