package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TriagePediatricDto extends TriageNoAdministrativeDto {

    //TODO add specific attributes

    @Builder(builderMethodName = "pediatricBuilder")
    public TriagePediatricDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId, notes);
    }
}
