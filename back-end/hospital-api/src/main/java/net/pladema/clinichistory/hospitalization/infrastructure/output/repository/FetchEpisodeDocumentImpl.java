package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.dto.DocumentTypeDto;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.controller.dto.SavedEpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import net.pladema.clinichistory.hospitalization.service.domain.SavedEpisodeDocumentResponseBo;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FetchEpisodeDocumentImpl implements FetchEpisodeDocument {

	private EpisodeDocumentStorage episodeDocumentStorage;

	private final String OUTPUT = "Output -> {}";

	public FetchEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage) {
		this.episodeDocumentStorage = episodeDocumentStorage;
	}

	@Override
	public SavedEpisodeDocumentResponseDto saveEpisodeDocument(EpisodeDocumentDto dto) {
		log.debug("Input parameters -> dto {}", dto);
		SavedEpisodeDocumentResponseBo bo = this.episodeDocumentStorage.saveEpisodeDocument(new EpisodeDocumentBo(dto.getFile(), dto.getEpisodeDocumentTypeId(), dto.getInternmentEpisodeId()));
		SavedEpisodeDocumentResponseDto episodeDocumentResponseDto = this.mapBoToDto(bo);
		log.debug(OUTPUT, episodeDocumentResponseDto);
		return episodeDocumentResponseDto;
	}

	@Override
	public List<EpisodeDocumentResponseDto> getEpisodeDocuments(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		List<EpisodeDocumentResponseDto> result =  this.episodeDocumentStorage.getEpisodeDocuments(internmentEpisodeId)
				.stream()
				.map(bo -> this.mapToDto(bo))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<DocumentTypeDto> getDocumentTypes() {
		return this.episodeDocumentStorage.getDocumentTypes()
				.stream()
				.map(bo -> this.mapDocumentTypeToDto(bo))
				.collect(Collectors.toList());
	}

	@Override
	public boolean deleteDocument(Integer episodeDocumentId) {
		return this.episodeDocumentStorage.deleteDocument(episodeDocumentId);
	}

	private DocumentTypeDto mapDocumentTypeToDto(DocumentTypeBo bo) {
		return new DocumentTypeDto(bo.getId(), bo.getDescription());
	}

	private SavedEpisodeDocumentResponseDto mapBoToDto(SavedEpisodeDocumentResponseBo bo) {
		return new SavedEpisodeDocumentResponseDto(
				bo.getId(),
				bo.getFilePath(),
				bo.getFileName(),
				bo.getUuidFile(),
				bo.getCreatedOn(),
				bo.getEpisodeDocumentType(),
				bo.getInternmentEpisodeId()
		);
	}

	private EpisodeDocumentResponseDto mapToDto(EpisodeDocumentResponseBo bo) {
		return new EpisodeDocumentResponseDto(
				bo.getId(),
				bo.getDescription(),
				bo.getFileName(),
				bo.getCreatedOn()
		);
	}
}
