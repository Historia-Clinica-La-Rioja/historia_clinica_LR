package net.pladema.internation.repository.ips.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.auditable.entity.AuditableEntity;
import net.pladema.auditable.listener.AuditListener;
import net.pladema.internation.repository.listener.InternationAuditableEntity;
import net.pladema.internation.repository.listener.InternationListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "inmunization")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Inmunization extends InternationAuditableEntity {

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

	@Column(name = "category_id", length = 20, nullable = false)
	private String categoryId;

	@Column(name = "expiration_date")
	private LocalDate expirationDate;

	@Column(name = "administration_date", nullable = false)
	private LocalDate administrationDate;

	@Column(name = "note_id")
	private Long noteId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Inmunization that = (Inmunization) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId) &&
				sctidCode.equals(that.sctidCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, sctidCode);
	}
}
