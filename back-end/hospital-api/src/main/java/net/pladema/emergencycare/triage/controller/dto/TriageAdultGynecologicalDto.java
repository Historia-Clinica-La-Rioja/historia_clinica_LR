package net.pladema.emergencycare.triage.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class TriageAdultGynecologicalDto extends TriageNoAdministrativeDto {

    private NewRiskFactorsObservationDto riskFactors;

    @Builder(builderMethodName = "adultGynecologicalBuilder")
    public TriageAdultGynecologicalDto(Short categoryId, Integer doctorsOfficeId, String notes){
        super(categoryId, doctorsOfficeId, notes);
    }
}
