package net.pladema.emergencycare.triage.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.*;

@Entity
@Table(name = "triage")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Triage  extends SGXAuditableEntity {
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

	@Column(name = "notes")
	private String notes;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "healthcare_professional_id")
	private Integer healthcareProfessionalId;

	public Triage(TriageBo triageBo) {
		this.id = triageBo.getId();
		this.emergencyCareEpisodeId = triageBo.getEmergencyCareEpisodeId();
		this.notes = triageBo.getNotes();
		this.triageCategoryId = triageBo.getCategoryId();
		this.healthcareProfessionalId = triageBo.getProfessionalId();
	}
}
