package ar.lamansys.sgh.shared.infrastructure.input.service.observation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FhirObservationGroupInfoDto {

	private Integer id;
	private Integer patientId;
	private Integer diagnosticReportId;
	private List<FhirObservationInfoDto> observations;

}
