package net.pladema.clinichistory.documents.repository.generalstate.domain;

import lombok.*;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;

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

    private String severityId;

    private LocalDate date;

    public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String verificationId,
                             Short categoryId, LocalDate date) {
        super(id, snomed, statusId);
        this.verificationId = verificationId;
        this.severityId = null;
        this.categoryId = categoryId;
        this.date = date;
    }

	public AllergyConditionVo(Integer id, Snomed snomed, String statusId, String status,
							  String verificationId, String verification,
							  Short categoryId, LocalDate date) {
		this(id, snomed, statusId,verificationId, categoryId, date);
		this.setStatus(status);
		this.setVerification(verification);
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
