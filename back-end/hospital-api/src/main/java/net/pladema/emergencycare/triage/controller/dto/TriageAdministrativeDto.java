package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class TriageAdministrativeDto extends TriageDto {

    @Builder(builderMethodName = "administrativeBuilder")
    public TriageAdministrativeDto(Short categoryId, Integer doctorsOfficeId){
        super(categoryId, doctorsOfficeId);
    }
}
