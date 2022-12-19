package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;

import org.springframework.web.multipart.MultipartFile;

public interface FetchEpisodeDocument {

	EpisodeDocumentResponseDto saveEpisodeDocument(EpisodeDocumentDto dto);
}
