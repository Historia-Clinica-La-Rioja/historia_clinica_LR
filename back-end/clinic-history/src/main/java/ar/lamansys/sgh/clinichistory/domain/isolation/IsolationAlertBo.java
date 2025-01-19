package ar.lamansys.sgh.clinichistory.domain.isolation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IsolationAlertBo {
	private Integer id;
	private Integer healthConditionId;
	private String healthConditionSctid;
	private String healthConditionPt;
	private List<Short> typeIds;
	private Short criticalityId;
	private LocalDate endDate;
	private String observations;
	private Short statusId;

	private Integer updatedById;
	private IsolationAlertAuthorBo updatedBy;
	private LocalDateTime updatedOn;
	//If true, the alert was updated after the evolution note that it's attached
	//to was created.
	private Boolean isModified;
}
