package net.pladema.patient.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LimitedPatientSearchDto {

    private List<PatientSearchDto> patientList;
    private Integer actualPatientSearchSize;
}
