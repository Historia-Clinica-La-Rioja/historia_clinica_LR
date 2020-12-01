package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.*;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ConditionVerificationStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "health_condition")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HealthCondition extends SGXAuditableEntity implements Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "sctid_code", length = 20, nullable = false)
	private String sctidCode;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId;

	@Column(name = "verification_status_id", length = 20, nullable = false)
	private String verificationStatusId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "inactivation_date")
	private LocalDate inactivationDate;

	@Column(name = "main", nullable = false)
	private Boolean main = false;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "problem_id", length = 20, nullable = false)
	private String problemId;

	public void setVerificationStatusId(String verificationId) {
		this.verificationStatusId = verificationId;
		if (verificationId == null)
			this.verificationStatusId = ConditionVerificationStatus.CONFIRMED;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HealthCondition that = (HealthCondition) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId) &&
				sctidCode.equals(that.sctidCode) &&
				startDate.equals(that.startDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, sctidCode, startDate);
	}

	@Override
	public Object clone() {
		HealthCondition result = null;
		try {
			result = (HealthCondition) super.clone();
		} catch (CloneNotSupportedException e) {
			result = new HealthCondition(
					this.getId(),this.getPatientId(), this.getSctidCode(),
					this.getStatusId(), this.getVerificationStatusId(),
					this.getStartDate(), this.getInactivationDate(), this.getMain(),
					this.getNoteId(), this.getProblemId()
			);
		}
		return result;
	}

}
