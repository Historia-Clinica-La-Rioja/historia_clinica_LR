package ar.lamansys.sgh.clinichistory.domain.isolation;


import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;
import ar.lamansys.sgx.shared.dates.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
public class IsolationAlertForPdfDocumentBo {
	private Integer id;
	private String healthConditionPt;
	private List<EIsolationType> types;
	private EIsolationCriticality criticality;
	private LocalDate endDate;
	private String observations;
	private EIsolationStatus status;
	private Integer updatedById;
	private IsolationAlertAuthorBo updatedBy;
	private LocalDateTime updatedOn;
	private Boolean isModified;

	public IsolationAlertForPdfDocumentBo(IsolationAlertBo alert) {
		this.id = alert.getId();
		this.healthConditionPt = alert.getHealthConditionPt();
		this.types = alert.getTypeIds().stream().map(EIsolationType::map).collect(Collectors.toList());
		this.criticality = EIsolationCriticality.map(alert.getCriticalityId());
		this.endDate = alert.getEndDate();
		this.observations = alert.getObservations();
		this.status = EIsolationStatus.map(alert.getStatusId());
		this.updatedBy = alert.getUpdatedBy();
		this.updatedById = alert.getUpdatedById();
		this.updatedOn = alert.getUpdatedOn();
		this.isModified = alert.getIsModified();
	}

	public LocalDateTime getUpdatedOnAtExpectedTimezone() {
		return DateUtils.fromUTCToZone(this.getUpdatedOn(), DateUtils.getAppTimezone());
	}
}
