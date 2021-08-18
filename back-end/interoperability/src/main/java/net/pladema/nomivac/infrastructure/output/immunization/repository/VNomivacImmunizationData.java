package net.pladema.nomivac.infrastructure.output.immunization.repository;

import ar.lamansys.sgx.shared.scheduling.infrastructure.output.repository.synchronization.AbstractData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Immutable
@Table(name = "v_nomivac_immunization_data")
@NoArgsConstructor
public class VNomivacImmunizationData implements Serializable, AbstractData<Integer> {

	private static final long serialVersionUID = 3848869082897825698L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "vaccine_sctid")
	private String vaccineSctid;

	@Column(name = "vaccine_pt")
	private String vaccinePt;

	@Column(name = "status_id")
	private String statusId;

	@Column(name = "administration_date")
	private LocalDate administrationDate;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "institution_id")
	private Integer institutionId;

	@Column(name = "condition_id")
	private Short conditionId;

	@Column(name = "condition_description")
	private String conditionDescription;

	@Column(name = "scheme_id")
	private Short schemeId;

	@Column(name = "scheme_description")
	private String schemeDescription;

	@Column(name = "dose")
	private String dose;

	@Column(name = "dose_order")
	private Short doseOrder;

	@Column(name = "lot_number")
	private String lotNumber;

	@Column(name = "note")
	private String note;

	@Column(name = "billable")
	private Boolean billable;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public VNomivacImmunizationData(Integer id, Integer patientId, String vaccineSctid, String vaccinePt,
									String statusId, LocalDate administrationDate, LocalDate expirationDate,
									Integer institutionId, Short conditionId, String conditionDescription,
									Short schemeId, String schemeDescription, String dose, Short doseOrder,
									String lotNumber, String note, Boolean billable, LocalDateTime updatedOn,
									Integer updatedBy) {
		this.id = id;
		this.patientId = patientId;
		this.vaccineSctid = vaccineSctid;
		this.vaccinePt = vaccinePt;
		this.statusId = statusId;
		this.administrationDate = administrationDate;
		this.expirationDate = expirationDate;
		this.institutionId = institutionId;
		this.conditionId = conditionId;
		this.conditionDescription = conditionDescription;
		this.schemeId = schemeId;
		this.schemeDescription = schemeDescription;
		this.dose = dose;
		this.doseOrder = doseOrder;
		this.lotNumber = lotNumber;
		this.note = note;
		this.billable = billable;
		this.updatedOn = updatedOn;
		this.updatedBy = updatedBy;
	}
}
