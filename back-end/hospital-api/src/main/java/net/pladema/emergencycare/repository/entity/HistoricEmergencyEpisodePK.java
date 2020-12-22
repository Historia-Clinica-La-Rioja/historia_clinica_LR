package net.pladema.emergencycare.repository.entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class HistoricEmergencyEpisodePK implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5497705866779374761L;

	@Column(name = "emergency_care_episode_id", nullable = false)
	private Integer emergencyCareEpisodeId;

	@Column(name = "change_state_date", nullable = false)
	private LocalDateTime changeStateDate;
}
