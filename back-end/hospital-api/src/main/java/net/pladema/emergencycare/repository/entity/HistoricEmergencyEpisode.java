package net.pladema.emergencycare.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.auditable.entity.SGXAuditListener;
import net.pladema.sgx.auditable.entity.SGXAuditableEntity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic_emergency_episode")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class HistoricEmergencyEpisode extends SGXAuditableEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 6849405640144726911L;

	@EmbeddedId
	private HistoricEmergencyEpisodePK pk;

	@Column(name = "emergency_care_state_id", nullable = false)
	private Short emergencyCareStateId;

	@Column(name = "doctors_office_id", nullable = false)
	private Integer doctorsOfficeId;

	public HistoricEmergencyEpisode(Integer emergencyCareEpisodeId, Short emergencyCareStateId, Integer doctorsOfficeId){
		pk = new HistoricEmergencyEpisodePK(emergencyCareEpisodeId, LocalDateTime.now());
		this.emergencyCareStateId = emergencyCareStateId;
		this.doctorsOfficeId = doctorsOfficeId;
	}
}
