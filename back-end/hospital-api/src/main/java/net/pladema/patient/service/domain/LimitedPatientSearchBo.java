package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LimitedPatientSearchBo {

    private List<PatientSearch> patientList;
    private Integer actualPatientSearchSize;
}
