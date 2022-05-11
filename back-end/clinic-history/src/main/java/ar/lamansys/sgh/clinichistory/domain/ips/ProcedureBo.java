package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureBo extends ClinicalTerm {

    private LocalDate performedDate;

    public ProcedureBo(ProcedureSummaryBo procedureSummaryBo){
        this.setSnomed(procedureSummaryBo.getSnomed());
        this.performedDate = procedureSummaryBo.getPerformedDate();
    }

    public ProcedureBo(SnomedBo snomed) {
        super(snomed);
    }

	public ProcedureBo(ProcedureVo procedureVo) {
		super();
		setId(procedureVo.getId());
		setSnomed(new SnomedBo(procedureVo.getSnomed()));
		setStatus(procedureVo.getStatusId());
		setPerformedDate(procedureVo.getPerformedDate());
	}

	public boolean equals (ClinicalTerm bo){
		boolean datesAreEquals = Optional.ofNullable(((ProcedureBo)bo).getPerformedDate())
				.map(p1 -> Optional.ofNullable(getPerformedDate())
						.map(p2-> p2.equals(p1))
						.orElse(false))
				.orElseGet(()-> getPerformedDate()==null);
		return super.equals(bo)
				&& datesAreEquals;
	}
}
