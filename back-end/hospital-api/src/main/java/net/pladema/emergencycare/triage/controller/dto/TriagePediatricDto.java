package net.pladema.emergencycare.triage.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;

import javax.annotation.Nullable;

import java.util.List;

@Getter
@NoArgsConstructor(force = true)
public class TriagePediatricDto extends TriageNoAdministrativeDto {

    @Nullable
    @JsonProperty("appearance")
    private final AppearanceDto appearance;

    @Nullable
    @JsonProperty("breathing")
    private final BreathingDto breathing;

    @Nullable
    @JsonProperty("circulation")
    private final CirculationDto circulation;

    @Builder(builderMethodName = "pediatricBuilder")
    public TriagePediatricDto(Short categoryId, Integer doctorsOfficeId, String notes,
                              AppearanceDto appearance, BreathingDto breathing, CirculationDto circulation, List<OutpatientReasonDto> reasons, Integer clinicalSpecialtySectorId){
        super(categoryId, doctorsOfficeId, notes, reasons, clinicalSpecialtySectorId);
        this.appearance = appearance;
        this.breathing = breathing;
        this.circulation = circulation;
    }
}
