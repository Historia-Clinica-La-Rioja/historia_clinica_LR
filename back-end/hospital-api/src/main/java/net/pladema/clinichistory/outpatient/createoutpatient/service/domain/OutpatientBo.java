package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import java.time.LocalDate;
import java.util.List;

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

    private List<ReasonBo> reasons;
}
