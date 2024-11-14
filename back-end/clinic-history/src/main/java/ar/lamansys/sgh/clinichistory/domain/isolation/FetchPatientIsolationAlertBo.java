package ar.lamansys.sgh.clinichistory.domain.isolation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationCriticality;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationType;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchPatientIsolationAlertBo {
	private Integer isolationAlertId;
	private Integer healthConditionId;
	private String healthConditionSctid;
	private String healthConditionPt;
	private List<EIsolationType> types;
	private EIsolationCriticality criticality;
	private LocalDateTime startDate;
	private LocalDate endDate;
	@Nullable
	private String observations;
	private Integer createdBy;
	private EIsolationStatus status;
	private ProfessionalInfoDto author;

	public EIsolationStatus getStatus() {
		var expired = LocalDate.now().isAfter(this.getEndDate());
		if (status != null && status.isOngoing() && expired)
			return EIsolationStatus.EXPIRED;
		return this.status;
	}

	public List<Short> getTypeIds() {
		return this.getTypes().stream().map(EIsolationType::getId).collect(Collectors.toList());
	}

	public Short getCriticalityId() {
		return this.getCriticality().getId();
	}

	public Short getStatusId() {
		return this.getStatus().getId();
	}

	public boolean isFinalized() {
		return this.getStatus() != null && (this.getStatus().isCancelled() || this.getStatus().isExpired());
	}
}
