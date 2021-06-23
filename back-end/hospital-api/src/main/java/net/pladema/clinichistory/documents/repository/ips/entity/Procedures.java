package net.pladema.clinichistory.documents.repository.ips.entity;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProceduresStatus;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.*;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "procedures")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Procedures extends SGXAuditableEntity<Integer> {

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

	@Column(name = "performed_date", nullable = false)
	private LocalDate performedDate;

	@Column(name = "note_id")
	private Long noteId;

	public Procedures(Integer patientId, Integer snomedId, String statusId, LocalDate performedDate, Long noteId) {
		super();
		this.patientId = patientId;
		this.snomedId = snomedId;
		if (statusId != null)
			this.statusId = statusId;
		this.performedDate = performedDate;
		this.noteId = noteId;
	}
}
