package net.pladema.emergencycare.triage.repository.entity;

import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.triage.service.domain.TriageBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;

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

	@Column(name = "notes")
	private String notes;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "doctors_office_id")
	private Integer doctorsOfficeId;

	public Triage(TriageBo triageBo) {
		this.id = triageBo.getId();
		this.emergencyCareEpisodeId = triageBo.getEmergencyCareEpisodeId();
		this.notes = triageBo.getNotes();
		this.triageCategoryId = triageBo.getCategoryId();
		this.doctorsOfficeId = triageBo.getDoctorsOfficeId();
	}
}
