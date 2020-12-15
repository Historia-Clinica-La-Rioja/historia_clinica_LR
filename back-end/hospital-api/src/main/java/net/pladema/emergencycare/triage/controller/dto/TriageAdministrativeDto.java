package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TriageAdministrativeDto extends TriageDto {

    //TODO add specific attribute

    @Builder(builderMethodName = "administrativeBuilder")
    public TriageAdministrativeDto(Short categoryId, Integer doctorsOfficeId){
        super(categoryId, doctorsOfficeId);
    }
}
