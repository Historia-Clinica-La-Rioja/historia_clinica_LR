package net.pladema.internation.repository.core.entity;

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
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

@Entity
@Table(name = "internment_episode")
@EntityListeners(InternationListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class InternmentEpisode extends InternationAuditableEntity {

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

	@Column(name = "bed_id", nullable = false)
	private Integer bedId;

	@Column(name = "clinical_specialty_id", length = 20, nullable = true)
	private Integer clinicalSpecialtyId;

	@Column(name = "status_id", nullable = false)
	private Short statusId;

	@Column(name = "note_id", nullable = true)
	private Long noteId;

	@Column(name = "anamnesis_doc_id", nullable = true)
	private Long anamnesisDocId;

	@Column(name = "epicrisis_doc_id", nullable = true)
	private Long epicrisisDocId;

	@Column(name = "entry_date")
	private LocalDate entryDate;

	@Column(name = "discharge_date")
	private LocalDate dischargeDate;

	private int totalInternmentDays(){
		LocalDate today = LocalDate.now();
		Period p = Period.between(getCreatedOn().toLocalDate(), today);
		return p.getDays();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		InternmentEpisode that = (InternmentEpisode) o;
		return id.equals(that.id) &&
				patientId.equals(that.patientId) &&
				bedId.equals(that.bedId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, patientId, bedId);
	}
}
