package net.pladema.clinichistory.hospitalization.application.createEpisodeDocument;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;
import net.pladema.clinichistory.hospitalization.service.impl.exceptions.MoreThanOneConsentDocumentException;

public interface CreateEpisodeDocument {

	Integer run(EpisodeDocumentDto dto) throws MoreThanOneConsentDocumentException;
}
