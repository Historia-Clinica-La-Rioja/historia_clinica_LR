package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutpatientBo {

    private Integer id;

    private Integer patientId;

    private Integer clinicalSpecialtyId;

    private Integer institutionId;

    private LocalDate startDate;

    private Long documentId;

    private Integer doctorId;

    private Boolean billable;

}
