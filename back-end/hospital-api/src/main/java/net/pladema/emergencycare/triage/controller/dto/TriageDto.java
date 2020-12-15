package net.pladema.emergencycare.triage.controller.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import net.pladema.patient.controller.dto.HealthInsuranceDto;
import net.pladema.patient.controller.dto.PrivateHealthInsuranceDto;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@JsonSubTypes({
        @JsonSubTypes.Type(value= TriageAdministrativeDto.class, name="TriageAdministrativeDto"),
        @JsonSubTypes.Type(value= TriageAdultGynecologicalDto.class, name="TriageAdultGynecologicalDto"),
        @JsonSubTypes.Type(value= TriagePediatricDto.class, name="TriagePediatricDto")
})
public abstract class TriageDto implements Serializable {

    private final Short categoryId;

    private final Integer doctorsOfficeId;

}
