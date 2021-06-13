package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientProblemDto;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AMedicalDischargeDto extends MedicalDischargeDto {

    private Short dischargeTypeId;

    private List<OutpatientProblemDto> problems = new ArrayList<>();
}
