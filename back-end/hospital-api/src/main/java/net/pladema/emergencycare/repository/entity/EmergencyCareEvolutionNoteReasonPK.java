package net.pladema.emergencycare.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EmergencyCareEvolutionNoteReasonPK implements Serializable {

	@Column(name = "emergency_care_evolution_note_id", nullable = false)
	private Integer emergencyCareEvolutionNoteId;

	@Column(name = "reason_id", nullable = false)
	private String reasonId;

}
