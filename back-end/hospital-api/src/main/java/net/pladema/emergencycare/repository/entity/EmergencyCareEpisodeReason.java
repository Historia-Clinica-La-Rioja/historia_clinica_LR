package net.pladema.emergencycare.repository.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "emergency_care_episode_reason")
@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class EmergencyCareEpisodeReason implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3635340556780266612L;

	@EmbeddedId
	private EmergencyCareEpisodeReasonPK pk;

	public EmergencyCareEpisodeReason(Integer emergencyCareEpisodeId, String reasonId){
		pk = new EmergencyCareEpisodeReasonPK(emergencyCareEpisodeId, reasonId);
	}
}
