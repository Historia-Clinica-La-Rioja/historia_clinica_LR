package ar.lamansys.sgh.clinichistory.domain.isolation;


import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class IsolationAlertForPdfDocumentBo {
	private Integer id;
	private String healthConditionPt;
	private List<EIsolationType> types;
	private EIsolationCriticality criticality;
	private LocalDate endDate;
	private String observations;
	private EIsolationStatus status;

	public IsolationAlertForPdfDocumentBo(IsolationAlertBo alert) {
		this.id = alert.getId();
		this.healthConditionPt = alert.getHealthConditionPt();
		this.types = alert.getTypeIds().stream().map(EIsolationType::map).collect(Collectors.toList());
		this.criticality = EIsolationCriticality.map(alert.getCriticalityId());
		this.endDate = alert.getEndDate();
		this.observations = alert.getObservations();
		this.status = EIsolationStatus.map(alert.getStatusId());
	}
}
