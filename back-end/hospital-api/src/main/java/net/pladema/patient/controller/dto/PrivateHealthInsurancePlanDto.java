package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.patient.service.domain.MedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateHealthInsurancePlanDto {

    private Integer id;

    private Integer privateHealthInsuranceId;

    private String plan;

}
