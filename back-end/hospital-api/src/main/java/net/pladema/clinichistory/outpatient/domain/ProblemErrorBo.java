package net.pladema.clinichistory.outpatient.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProblemErrorBo extends HealthConditionBo {
    private Short errorReasonId;
    private String errorObservations;
    private List<Integer> diagnosticReportsId;
    private List<Integer> serviceRequestsId;
    private List<Integer> appointmentsId;
    private List<Integer> referencesId;
}
