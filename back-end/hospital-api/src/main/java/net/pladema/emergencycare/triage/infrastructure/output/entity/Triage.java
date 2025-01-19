package net.pladema.emergencycare.triage.infrastructure.output.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.domain.TriageBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import net.pladema.establishment.domain.ClinicalSpecialtySectorBo;

import javax.persistence.*;

@Entity
@Table(name = "triage")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Triage  extends SGXAuditableEntity<Integer> {
	/**
	 *
	 */
	private static final long serialVersionUID = 7224767652514389036L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "emergency_care_episode_id", nullable = false)
	private Integer emergencyCareEpisodeId;

	@Column(name = "notes", columnDefinition = "TEXT")
	private String notes;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "doctors_office_id")
	private Integer doctorsOfficeId;

	@Column(name = "clinical_specialty_sector_id")
	private Integer clinicalSpecialtySectorId;

	public Triage(TriageBo triageBo) {
		this.id = triageBo.getTriageId();
		this.emergencyCareEpisodeId = triageBo.getEmergencyCareEpisodeId();
		this.notes = triageBo.getNotes() != null ? triageBo.getNotes().getOtherNote() : null;
		this.triageCategoryId = triageBo.getCategoryId();
		this.doctorsOfficeId = triageBo.getDoctorsOfficeId();
		ClinicalSpecialtySectorBo css = triageBo.getClinicalSpecialtySectorBo();
		this.clinicalSpecialtySectorId = css == null ? null : css.getId();
	}
}
