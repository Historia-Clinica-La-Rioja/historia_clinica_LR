package net.pladema.emergencycare.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "emergency_care_evolution_note_reason")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmergencyCareEvolutionNoteReason {

	@EmbeddedId
	private EmergencyCareEvolutionNoteReasonPK pk;

	public EmergencyCareEvolutionNoteReason(Integer outpatientConsultationId, String reasonId){
		pk = new EmergencyCareEvolutionNoteReasonPK(outpatientConsultationId, reasonId);
	}

}
