package net.pladema.clinichistory.documents.repository.ips.entity;

import lombok.*;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.ProceduresStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "procedures")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Procedure extends SGXAuditableEntity {

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

	@Column(name = "sctid_code", length = 20, nullable = false)
	private String sctidCode;

	@Column(name = "status_id", length = 20, nullable = false)
	private String statusId = ProceduresStatus.COMPLETE;

	@Column(name = "performed_date")
	private LocalDate performedDate;

	@Column(name = "note_id")
	private Long noteId;

	public Procedure(Integer patientId, String sctId, String statusId, LocalDate performedDate) {
		super();
		this.patientId = patientId;
		this.sctidCode = sctId;
		if (statusId != null)
			this.statusId = statusId;
		this.performedDate = performedDate;
	}
}
