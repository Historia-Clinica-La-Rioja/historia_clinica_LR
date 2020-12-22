package net.pladema.clinichistory.requests.medicalrequests.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MedicalRequestBo {

    private Integer patientId;

    private Integer doctorId;

    private Integer healthConditionId;

    private LocalDate requestDate;

    private String observations;

}
