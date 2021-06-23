package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.AllergyIntoleranceVerificationStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "allergy_intolerance")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AllergyIntolerance extends SGXAuditableEntity<Integer> {

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
	private String statusId = AllergyIntoleranceClinicalStatus.ACTIVE;

	@Column(name = "verification_status_id", length = 20, nullable = false)
	private String verificationStatusId = AllergyIntoleranceVerificationStatus.CONFIRMED;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "category_id")
	private Short categoryId;

	@Column(name = "type")
	private Short type;

	@Column(name = "criticality")
	private Short criticality;

	public AllergyIntolerance(Integer patientId, Integer snomedId, String cie10Codes, String statusId,
							  String verificationId, Short categoryId, Short criticalityId, LocalDate startDate){
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		this.cie10Codes = cie10Codes;
		if (statusId != null)
			this.statusId = statusId;
		if (verificationId != null)
			this.verificationStatusId = verificationId;
		this.categoryId = categoryId;
		this.criticality = criticalityId;
		this.startDate = startDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AllergyIntolerance that = (AllergyIntolerance) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId) &&
				snomedId.equals(that.snomedId) &&
				startDate.equals(that.startDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, snomedId, startDate);
	}
}
