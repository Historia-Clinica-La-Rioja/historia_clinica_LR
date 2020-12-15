package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class TriageNoAdministrativeDto extends TriageDto {

    String notes;

    public TriageNoAdministrativeDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId);
        this.notes = notes;
    }
}
