package net.pladema.emergencycare.triage.controller.dto;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
public abstract class TriageNoAdministrativeDto extends TriageDto {

    @Nullable
    String notes;

    public TriageNoAdministrativeDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId);
        this.notes = notes;
    }

    public TriageNoAdministrativeDto() { }
}
