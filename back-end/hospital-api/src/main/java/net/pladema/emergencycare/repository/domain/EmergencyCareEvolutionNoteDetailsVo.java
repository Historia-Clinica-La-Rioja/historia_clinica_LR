package net.pladema.emergencycare.repository.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNote;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareEvolutionNoteDetailsVo {
	private EmergencyCareEvolutionNote evolutionNote;
	private Document document;
}
