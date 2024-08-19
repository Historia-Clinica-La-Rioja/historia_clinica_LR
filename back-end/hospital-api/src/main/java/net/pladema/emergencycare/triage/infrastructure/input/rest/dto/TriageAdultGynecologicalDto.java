package net.pladema.emergencycare.triage.infrastructure.input.rest.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;
import net.pladema.emergencycare.triage.controller.dto.TriageNoAdministrativeDto;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class TriageAdultGynecologicalDto extends TriageNoAdministrativeDto {

    private NewRiskFactorsObservationDto riskFactors;

    @Builder(builderMethodName = "adultGynecologicalBuilder")
    public TriageAdultGynecologicalDto(Short categoryId, Integer doctorsOfficeId, String notes, List<OutpatientReasonDto> reasons){
        super(categoryId, doctorsOfficeId, notes, reasons);
    }
}
