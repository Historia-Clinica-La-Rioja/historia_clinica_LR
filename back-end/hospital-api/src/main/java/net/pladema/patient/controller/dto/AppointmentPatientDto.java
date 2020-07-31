package net.pladema.patient.controller.dto;

import lombok.*;
import net.pladema.person.controller.dto.IBasicPersonalData;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPatientDto {

    private Integer id;

    private IBasicPersonalData person;

    private String medicalCoverageName;

    private String medicalCoverageAffiliateNumber;
}
