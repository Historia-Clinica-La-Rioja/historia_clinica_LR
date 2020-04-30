package net.pladema.internation.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;
import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import net.pladema.internation.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "allergy_intolerance")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyIntolerance extends InternationAuditableEntity {

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
	private String statusId = AllergyIntoleranceClinicalStatus.ACTIVE;

	@Column(name = "verification_status_id", length = 20, nullable = false)
	private String verificationStatusId = AllergyIntoleranceVerificationStatus.CONFIRMED;

	@Column(name = "category_id", length = 20, nullable = false)
	private String categoryId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "note_id")
	private Long noteId;

	public AllergyIntolerance(Integer patientId, String sctidCode, String statusId,
							  String verificationId, String categoryId, LocalDate startDate){
		super();
		this.patientId = patientId;
		this.sctidCode = sctidCode;
		if (statusId != null)
			this.statusId = statusId;
		if (verificationId != null)
			this.verificationStatusId = verificationId;
		this.categoryId = categoryId;
		this.startDate = startDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AllergyIntolerance that = (AllergyIntolerance) o;
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
