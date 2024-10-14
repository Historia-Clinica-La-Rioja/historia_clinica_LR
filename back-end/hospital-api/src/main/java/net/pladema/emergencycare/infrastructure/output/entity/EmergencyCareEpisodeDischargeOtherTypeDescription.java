package net.pladema.emergencycare.infrastructure.output.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="emergency_care_discharge_other_type_description")
@Entity
public class EmergencyCareEpisodeDischargeOtherTypeDescription implements Serializable {

	private static final long serialVersionUID = 1829475928374612345L;

	@Id
	@Column(name = "emergency_care_episode_id", nullable = false)
	private Integer emergencyCareEpisodeId;

	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;
}
