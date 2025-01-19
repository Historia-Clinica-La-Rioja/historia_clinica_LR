package net.pladema.emergencycare.triage.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientReasonDto;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class TriageAdministrativeDto extends TriageDto {

    @Builder(builderMethodName = "administrativeBuilder")
    public TriageAdministrativeDto(Short categoryId, Integer doctorsOfficeId, List<OutpatientReasonDto> reasons, Integer clinicalSpecialtySectorId){
        super(categoryId, doctorsOfficeId, reasons, clinicalSpecialtySectorId);
    }
}
