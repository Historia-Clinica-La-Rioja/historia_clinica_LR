package net.pladema.violencereport.infrastructure.output.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(SGXAuditListener.class)
@Table(name = "violence_report")
@Entity
public class ViolenceReport extends SGXAuditableEntity<Integer> implements Serializable {

	private static final long serialVersionUID = -6709810449897033483L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "patient_id", nullable = false)
	private Integer patientId;

	@Column(name = "situation_id", nullable = false)
	private Short situationId;

	@Column(name = "evolution_id", nullable = false)
	private Short evolutionId;

	@Column(name = "can_read_and_write")
	private Boolean canReadAndWrite;

	@Column(name = "has_income")
	private Boolean hasIncome;

	@Column(name = "works_at_formal_sector")
	private Boolean worksAtFormalSector;

	@Column(name = "has_social_plan")
	private Boolean hasSocialPlan;

	@Column(name = "has_disability")
	private Boolean hasDisability;

	@Column(name = "disability_certificate_status_id")
	private Short disabilityCertificateStatusId;

	@Column(name = "is_institutionalized")
	private Boolean isInstitutionalized;

	@Column(name = "institutionalized_details", length = 200)
	private String institutionalizedDetails;

	@Column(name = "lack_of_legal_capacity", nullable = false)
	private Boolean lackOfLegalCapacity;

	@Column(name = "episode_date", nullable = false)
	private LocalDate episodeDate;

	@Column(name = "violence_towards_underage_type_id", nullable = false)
	private Short violenceTowardsUnderageTypeId;

	@Column(name = "schooled")
	private Boolean schooled;

	@Column(name = "school_level_id")
	private Short schoolLevelId;

	@Column(name = "risk_level_id", nullable = false)
	private Short riskLevelId;

	@Column(name = "coordination_inside_health_sector", nullable = false)
	private Boolean coordinationInsideHealthSector;

	@Column(name = "coordination_within_health_system")
	private Boolean coordinationWithinHealthSystem;

	@Column(name = "coordination_within_health_institution")
	private Boolean coordinationWithinHealthInstitution;

	@Column(name = "internment_indicated_status_id")
	private Short internmentIndicatedStatusId;

	@Column(name = "coordination_with_other_social_organizations")
	private Boolean coordinationWithOtherSocialOrganizations;

	@Column(name = "were_previous_episode_with_victim_or_keeper", nullable = false)
	private Boolean werePreviousEpisodeWithVictimOrKeeper;

	@Column(name = "institution_reported", nullable = false)
	private Boolean institutionReported;

	@Column(name = "was_sexual_violence", nullable = false)
	private Boolean wasSexualViolence;

	@Column(name = "observations", columnDefinition = "TEXT")
	private String observations;

	@Column(name = "institution_id", nullable = false)
	private Integer institutionId;

}
