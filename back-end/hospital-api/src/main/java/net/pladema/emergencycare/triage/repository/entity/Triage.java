package net.pladema.emergencycare.triage.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

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
	private Integer id;

	@Column(name = "emergency_care_episode_id", nullable = false)
	private Integer emergencyCareEpisodeId;

	@Column(name = "notes")
	private String notes;

	@Column(name = "triage_category_id", nullable = false)
	private Short triageCategoryId;

	@Column(name = "healthcare_professional_id")
	private Integer healthcareProfessionalId;
}
