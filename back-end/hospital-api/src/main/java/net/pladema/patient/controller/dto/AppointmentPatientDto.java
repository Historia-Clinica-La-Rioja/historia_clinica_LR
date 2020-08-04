package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.staff.controller.dto.BasicPersonalDataDto;


@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentPatientDto {

    private Integer id;

    private BasicPersonalDataDto person;

    private String medicalCoverageName;

    private String medicalCoverageAffiliateNumber;
}
