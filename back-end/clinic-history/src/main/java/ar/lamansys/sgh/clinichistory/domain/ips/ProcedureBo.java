package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.hce.summary.ProcedureSummaryBo;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.ProcedureVo;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProcedureBo extends ClinicalTerm implements IpsBo {

    private LocalDate performedDate;

	@Nullable
	private ProcedureTypeEnum type = ProcedureTypeEnum.PROCEDURE;
	
	private Boolean isPrimary = Boolean.TRUE;

	@Nullable
	private String note;
	
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
		setIsPrimary(procedureVo.getIsPrimary());
		setNote(procedureVo.getNote());
		setType(ProcedureTypeEnum.map(procedureVo.getProcedureTypeId()));
	}

	public boolean equals (ClinicalTerm bo){
		boolean datesAreEquals = Optional.ofNullable(((ProcedureBo)bo).getPerformedDate())
				.map(p1 -> Optional.ofNullable(getPerformedDate())
						.map(p2-> p2.equals(p1))
						.orElse(false))
				.orElseGet(()-> getPerformedDate()==null);
		boolean notesAreEquals = Optional.ofNullable(((ProcedureBo)bo).getNote())
				.map(p1 -> Optional.ofNullable(getNote())
						.map(p2-> p2.equals(p1))
						.orElse(false))
				.orElseGet(()-> getNote()==null);
		return super.equals(bo)
				&& datesAreEquals
				&& notesAreEquals;
	}

	@Override
	public void accept(IpsVisitor visitor) {
		visitor.visitProcedure(this);
	}
}
