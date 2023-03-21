package net.pladema.clinichistory.hospitalization.application.createEpisodeDocument;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;

public interface CreateEpisodeDocument {

	Integer run(EpisodeDocumentDto dto);
}
