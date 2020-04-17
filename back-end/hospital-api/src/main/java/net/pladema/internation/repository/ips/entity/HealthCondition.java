package net.pladema.internation.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "health_condition")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthCondition extends InternationAuditableEntity {

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

	@Column(name = "problem_type_id", length = 20, nullable = false)
	private String problemTypeId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "inactivation_date")
	private LocalDate inactivationDate;

	@Column(name = "personal")
	private Boolean personal;

	@Column(name = "note_id")
	private Long noteId;

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

}
