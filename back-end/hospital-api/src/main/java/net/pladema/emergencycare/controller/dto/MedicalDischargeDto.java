package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

@NoArgsConstructor
@Getter
@Setter
public class MedicalDischargeDto {

    private DateTimeDto medicalDischargeOn;

    private Boolean autopsy;
}
