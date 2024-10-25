package ar.lamansys.sgh.clinichistory.domain.isolation;

import java.time.LocalDate;
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

}
