package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import net.pladema.clinichistory.hospitalization.controller.dto.DocumentTypeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.controller.dto.SavedEpisodeDocumentResponseDto;

import java.util.List;

public interface FetchEpisodeDocument {

	SavedEpisodeDocumentResponseDto saveEpisodeDocument(EpisodeDocumentDto dto);

	List<EpisodeDocumentResponseDto> getEpisodeDocuments(Integer internmentEpisodeId);

	List<DocumentTypeDto> getDocumentTypes();
}
