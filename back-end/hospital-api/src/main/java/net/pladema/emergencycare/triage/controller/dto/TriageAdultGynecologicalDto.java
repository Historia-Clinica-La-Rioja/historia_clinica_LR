package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TriageAdultGynecologicalDto extends TriageNoAdministrativeDto {

    //TODO add specific attributes

    @Builder(builderMethodName = "adultGynecologicalBuilder")
    public TriageAdultGynecologicalDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId, notes);
    }
}
