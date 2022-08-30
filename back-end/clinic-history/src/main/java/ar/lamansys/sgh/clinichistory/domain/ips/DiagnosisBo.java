package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiagnosisBo extends HealthConditionBo {

    private boolean presumptive = false;

    public DiagnosisBo(SnomedBo snomed) {
        super(snomed);
    }

    public boolean isPresumptive() {
        if (getVerificationId() != null && ConditionVerificationStatus.isDownState(getVerificationId()))
            return false;
        return presumptive;
    }
	@Override
	public boolean equals(ClinicalTerm bo){
		return super.equals(bo)&&((DiagnosisBo)bo).isPresumptive()==isPresumptive();
	}
}
