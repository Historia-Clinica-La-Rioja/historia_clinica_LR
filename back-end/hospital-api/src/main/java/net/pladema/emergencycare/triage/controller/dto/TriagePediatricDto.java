package net.pladema.emergencycare.triage.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class TriagePediatricDto extends TriageNoAdministrativeDto {

    @JsonProperty("appearance")
    private final AppearanceDto appearance;

    @JsonProperty("breathing")
    private final BreathingDto breathing;

    @JsonProperty("circulation")
    private final CirculationDto circulation;

    @Builder(builderMethodName = "pediatricBuilder")
    public TriagePediatricDto(Short categoryId, Integer doctorsOfficeId, String notes,
                              AppearanceDto appearance, BreathingDto breathing, CirculationDto circulation){
        super(categoryId, doctorsOfficeId, notes);
        this.appearance = appearance;
        this.breathing = breathing;
        this.circulation = circulation;
    }
}
