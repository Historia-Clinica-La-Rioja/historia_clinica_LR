package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllergyConditionVo extends ClinicalTermVo {

    private String verificationId;

    private String verification;

    private Short categoryId;

    private Short criticalityId;

    private LocalDate date;

    public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String verificationId,
							  Short categoryId, Short criticalityId, LocalDate date) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.criticalityId = criticalityId;
        this.categoryId = categoryId;
        this.date = date;
    }

	public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String status,
							  String verificationId, String verification,
							  Short categoryId, Short criticalityId, LocalDate date) {
		this(id, snomed, statusId, verificationId, categoryId, criticalityId, date);
		this.setStatus(status);
		this.setVerification(verification);
	}
    
	@Override
	public int hashCode() {
		return Objects.hash(verificationId, categoryId, criticalityId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllergyConditionVo other = (AllergyConditionVo) obj;
		return Objects.equals(verificationId, other.getVerificationId()) && 
			   Objects.equals(categoryId, other.getCategoryId()) &&
			   Objects.equals(criticalityId, other.getCriticalityId());
	}
}
