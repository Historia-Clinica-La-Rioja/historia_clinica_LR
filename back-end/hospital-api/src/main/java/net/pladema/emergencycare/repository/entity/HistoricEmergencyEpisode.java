package net.pladema.emergencycare.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;
import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;

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
public class HistoricEmergencyEpisode extends SGXAuditableEntity<HistoricEmergencyEpisodePK> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6849405640144726911L;

	@EmbeddedId
	private HistoricEmergencyEpisodePK pk;

	@Column(name = "emergency_care_state_id", nullable = false)
	private Short emergencyCareStateId;

	@Column(name = "doctors_office_id")
	private Integer doctorsOfficeId;

	public HistoricEmergencyEpisode(HistoricEmergencyEpisodeBo historicEmergencyEpisodeBo) {
		this.pk = new HistoricEmergencyEpisodePK(historicEmergencyEpisodeBo.getEmergencyCareEpisodeId(), LocalDateTime.now());
		this.emergencyCareStateId = historicEmergencyEpisodeBo.getEmergencyCareStateId();
		this.doctorsOfficeId = historicEmergencyEpisodeBo.getDoctorsOfficeId();
	}

	public LocalDateTime getChangeStateDate() {
		return this.pk.getChangeStateDate();
	}

	@Override
	public HistoricEmergencyEpisodePK getId() {
		return this.pk;
	}
}
