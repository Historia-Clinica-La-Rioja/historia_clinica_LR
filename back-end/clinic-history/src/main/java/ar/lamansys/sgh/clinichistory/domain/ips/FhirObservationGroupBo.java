package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FhirObservationGroupBo {

	private Integer id;
	private Integer patientId;
	private Integer diagnosticReportId;
	private List<FhirObservationBo> observations;

}
