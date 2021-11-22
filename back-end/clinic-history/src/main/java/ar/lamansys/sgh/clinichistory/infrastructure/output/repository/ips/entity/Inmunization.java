package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.InmunizationStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "inmunization")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Inmunization extends SGXAuditableEntity<Integer> {

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

	@Column(name = "cie10_codes")
	private String cie10Codes;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = InmunizationStatus.COMPLETE;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "administration_date")
	private LocalDate administrationDate;

 	@Column(name = "condition_id")
	private Short conditionId;

	@Column(name = "scheme_id")
	private Short schemeId;

	@Column(name = "dose")
	private String dose;

	@Column(name = "dose_order")
	private Short doseOrder;

	@Column(name = "institution_id")
	private Integer institutionId;

	@Column(name = "lot_number")
	private String lotNumber;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "billable")
	private Boolean billable;

	@Column(name = "institution_info")
	private String institutionInfo;

	@Column(name = "doctor_info")
	private String doctorInfo;

	public Inmunization(Integer patientId,
						Integer snomedId,
						String cie10Codes, String statusId,
						LocalDate administrationDate,
						Integer institutionId,
						String institutionInfo,
						String doctorInfo,
						Short conditionId,
						Short schemeId,
						String dose,
						Short doseOrder,
						Long noteId,
						String lotNumber,
						boolean billable) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		this.cie10Codes = cie10Codes;
		if (statusId != null)
			this.statusId = statusId;
		this.noteId = noteId;
		this.institutionId = institutionId;
		this.institutionInfo = institutionInfo;
		this.doctorInfo = doctorInfo;
		this.conditionId = conditionId;
		this.schemeId = schemeId;
		this.dose = dose;
		this.doseOrder = doseOrder;
		this.lotNumber = lotNumber;
		this.administrationDate = administrationDate;
		this.billable = billable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Inmunization that = (Inmunization) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId) &&
				snomedId.equals(that.snomedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, snomedId);
	}
}
