package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class ProcedureVo extends ClinicalTermVo {

	private LocalDate performedDate;

	private Boolean isPrimary = Boolean.TRUE;

	private Short procedureTypeId;

	private String note;

	public ProcedureVo(Integer id, Snomed snomed, String statusId, LocalDate performedDate, Short procedureTypeId) {
		super(id, snomed, statusId);
		this.performedDate = performedDate;
		this.procedureTypeId = procedureTypeId;
	}

	public ProcedureVo(Integer id, Snomed snomed, String statusId, LocalDate performedDate, Boolean isPrimary, String note, Short procedureTypeId) {
		this(id, snomed, statusId, performedDate, procedureTypeId);
		this.isPrimary = isPrimary;
		this.note = note;
		this.procedureTypeId = procedureTypeId;
	}
}
