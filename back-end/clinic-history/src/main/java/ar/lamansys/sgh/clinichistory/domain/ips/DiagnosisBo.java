package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProblemTypeEnum;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DiagnosisBo extends HealthConditionBo implements IpsBo {

    private boolean presumptive = false;

	private ProblemTypeEnum type = ProblemTypeEnum.DIAGNOSIS;

    public DiagnosisBo(SnomedBo snomed) {
        super(snomed);
    }

    public boolean isPresumptive() {
        if (getVerificationId() != null && ConditionVerificationStatus.isDownState(getVerificationId()))
            return false;
        return presumptive;
    }

    @Override
    public String toString() {
        return "DiagnosisBo{" + "presumptive=" + presumptive +
                ", type=" + type +
                ", super=" + super.toString() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSnomedSctid(), getSnomed());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiagnosisBo otherDiagnosis = (DiagnosisBo) o;
        return this.specificEquals(otherDiagnosis);
    }

	private boolean specificEquals(ClinicalTerm bo){
		return super.equals(bo)&&((DiagnosisBo)bo).isPresumptive()==isPresumptive();
	}

	public DiagnosisBo(HealthConditionBo healthCondition) {
		super(healthCondition.getSnomed());
		this.setVerificationId(healthCondition.getVerificationId());
		this.setVerification(healthCondition.getVerification());
		this.setMain(healthCondition.isMain());
		this.setStatusId(healthCondition.getStatusId());
		this.setStatus(healthCondition.getStatus());
		this.setCie10codes(healthCondition.getCie10codes());
		this.setId(healthCondition.getId());
		this.setPatientId(healthCondition.getPatientId());
		this.setPresumptive(ConditionVerificationStatus.PRESUMPTIVE.equals(healthCondition.getVerificationId()));
	}

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitDiagnosis(this);
    }
}
