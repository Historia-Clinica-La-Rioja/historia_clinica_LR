package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProcedureTypeEnum;
import ar.lamansys.sgx.shared.migratable.SGXDocumentEntity;
import lombok.*;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "procedures")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Procedure extends SGXAuditableEntity<Integer> implements SGXDocumentEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -3053291021636483828L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "snomed_id", nullable = false)
	private Integer snomedId;

	@Column(name = "cie10_codes", length = 255, nullable = true)
	private String cie10Codes;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = ProceduresStatus.COMPLETE;

	@Column(name = "performed_date")
	private LocalDate performedDate;

	@Column(name = "note_id")
	private Long noteId;

	@Column(name = "procedure_type_id")
	private Short procedureTypeId;

	@Column(name = "is_primary")
	private boolean isPrimary = Boolean.TRUE;

	public Procedure(Integer patientId, Integer snomedId, String statusId, LocalDate performedDate, ProcedureTypeEnum procedureType, Boolean isPrimary) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		if (statusId != null)
			this.statusId = statusId;
		this.performedDate = performedDate;
		this.procedureTypeId = procedureType.getId();
		this.isPrimary = isPrimary;
	}
}
