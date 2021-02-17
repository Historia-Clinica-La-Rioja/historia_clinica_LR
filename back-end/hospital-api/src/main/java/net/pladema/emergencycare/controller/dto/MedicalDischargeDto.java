package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientProblemDto;
import net.pladema.sgx.dates.controller.dto.DateTimeDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class MedicalDischargeDto {

    private DateTimeDto medicalDischargeOn;

    private List<OutpatientProblemDto> problems = new ArrayList<>();

    private Boolean autopsy;

    private Short dischargeTypeId;
}
