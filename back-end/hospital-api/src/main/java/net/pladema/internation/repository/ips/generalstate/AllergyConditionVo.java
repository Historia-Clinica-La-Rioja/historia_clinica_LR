package net.pladema.internation.repository.ips.generalstate;

import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.masterdata.entity.Snomed;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AllergyConditionVo extends ClinicalTermVo {

    private String verificationId;

    private String categoryId;

    private String severityId;

    private LocalDate date;

    public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String verificationId,
                             String categoryId, LocalDate date) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.severityId = null;
        this.categoryId = categoryId;
        this.date = date;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(verificationId, categoryId, severityId);
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
			   Objects.equals(severityId, other.getSeverityId());
	}
}
