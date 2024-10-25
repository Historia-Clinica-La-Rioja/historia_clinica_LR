package ar.lamansys.sgh.clinichistory.domain.isolation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
}
