package net.pladema.emergencycare.triage.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewRiskFactorsObservationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.controller.dto.EmergencyCareUserDto;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TriageListDto implements Serializable {

    private Integer id;

    private DateTimeDto creationDate;

    private TriageCategoryDto category;

    private EmergencyCareUserDto createdBy;

    private DoctorsOfficeDto doctorsOffice;

    private NewRiskFactorsObservationDto riskFactors;

    private TriageAppearanceDto appearance;

    private TriageBreathingDto breathing;

    private TriageCirculationDto circulation;

    private String notes;

}
