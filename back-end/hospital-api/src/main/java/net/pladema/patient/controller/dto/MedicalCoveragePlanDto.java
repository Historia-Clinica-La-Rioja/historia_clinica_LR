package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalCoveragePlanDto {

    private Integer id;

    private Integer medicalCoverageId;

    private String plan;

}
