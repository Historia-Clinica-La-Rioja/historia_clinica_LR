package net.pladema.emergencycare.controller.document.documentsearch.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.emergencycare.controller.dto.EmergencyCareEvolutionNoteDocumentDto;
import net.pladema.emergencycare.triage.controller.dto.TriageDocumentDto;

import java.util.List;

@Getter
@Setter
public class EmergencyCareHistoricDocumentDto {

	private List<TriageDocumentDto> triages;

	private List<EmergencyCareEvolutionNoteDocumentDto> evolutionNotes;

	public EmergencyCareHistoricDocumentDto(List<TriageDocumentDto> triages, List<EmergencyCareEvolutionNoteDocumentDto> evolutionNotes) {
		this.triages = triages;
		this.evolutionNotes = evolutionNotes;
	}

}
