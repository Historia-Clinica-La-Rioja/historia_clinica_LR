package net.pladema.emergencycare.controller.document.documentsearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmergencyCareHistoricDocumentDto {

	private List<EmergencyCareEpisodeTriageSearchDto> triages;

	public EmergencyCareHistoricDocumentDto(List<EmergencyCareEpisodeTriageSearchDto> triages) {
		this.triages = triages;
	}

}
