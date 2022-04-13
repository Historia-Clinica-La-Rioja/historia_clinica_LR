package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureVo  extends ClinicalTermVo {

	private LocalDate performedDate;

	public ProcedureVo(Integer id, Snomed snomed, String statusId, LocalDate performedDate) {
		super(id, snomed, statusId);
		this.performedDate = performedDate;
	}

}
