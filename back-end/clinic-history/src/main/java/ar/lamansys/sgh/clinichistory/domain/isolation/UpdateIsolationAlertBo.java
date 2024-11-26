package ar.lamansys.sgh.clinichistory.domain.isolation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateIsolationAlertBo {
	String observations;
	Short criticalityId;
	LocalDate endDate;
}
