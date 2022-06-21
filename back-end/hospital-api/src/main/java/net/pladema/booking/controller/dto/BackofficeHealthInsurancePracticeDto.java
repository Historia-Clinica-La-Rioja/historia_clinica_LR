package net.pladema.booking.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BackofficeHealthInsurancePracticeDto {
    private Integer id;
    private Integer clinicalSpecialtyId;
    private Integer mandatoryMedicalPracticeId;
    private Integer medicalCoverageId;
    private String coverageInformation;
}
