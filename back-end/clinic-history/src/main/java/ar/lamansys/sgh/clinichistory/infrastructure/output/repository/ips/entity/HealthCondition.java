package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import lombok.*;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
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
public class HealthCondition extends SGXAuditableEntity<Integer> implements Cloneable{

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

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "cie10_codes", length = 255, nullable = true)
	private String cie10Codes;

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

	@Column(name = "severity", length = 10)
	private String severity;

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
				snomedId.equals(that.snomedId) &&
				startDate.equals(that.startDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, snomedId, startDate);
	}

	@Override
	public Object clone() {
		HealthCondition result = null;
		try {
			result = (HealthCondition) super.clone();
		} catch (CloneNotSupportedException e) {
			result = new HealthCondition(
					this.getId(),this.getPatientId(),
					this.getSnomedId(), this.getCie10Codes(),
					this.getStatusId(), this.getVerificationStatusId(),
					this.getStartDate(), this.getInactivationDate(), this.getMain(),
					this.getNoteId(), this.getProblemId(),
					this.getSeverity()
			);
		}
		return result;
	}

}
