package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;

import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentResponseDto;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FetchEpisodeDocumentImpl implements FetchEpisodeDocument {

	private EpisodeDocumentStorage episodeDocumentStorage;

	private final FileService fileService;

	private static final String RELATIVE_DIRECTORY = "/internments/{internmentEpisodeId}/episodedocuments/";

	private final String OUTPUT = "Output -> {}";

	public FetchEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage, FileService fileService) {
		this.episodeDocumentStorage = episodeDocumentStorage;
		this.fileService = fileService;
	}

	@Override
	public EpisodeDocumentResponseDto saveEpisodeDocument(EpisodeDocumentDto dto) {
		log.debug("Input parameters -> dto {}", dto);
		String newFileName = fileService.createFileName(FilenameUtils.getExtension(dto.getFile().getOriginalFilename()));
		String partialPath = buildPartialPath(dto.getInternmentEpisodeId(), newFileName);
		String uuid = newFileName.split("\\.")[0];
		try {
			fileService.transferMultipartFile(partialPath, uuid, "DOCUMENTO_EPISODIO", dto.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		EpisodeDocumentResponseBo bo = this.episodeDocumentStorage.saveEpisodeDocument(new EpisodeDocumentBo(partialPath, StringUtils.cleanPath(dto.getFile().getOriginalFilename()), uuid, dto.getEpisodeDocumentTypeId(), dto.getInternmentEpisodeId()));
		EpisodeDocumentResponseDto episodeDocumentResponseDto = this.mapToDto(bo);
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

	private EpisodeDocumentResponseDto mapToDto(EpisodeDocumentResponseBo bo) {
		return new EpisodeDocumentResponseDto(
				bo.getId(),
				bo.getFilePath(),
				bo.getFileName(),
				bo.getUuidFile(),
				bo.getCreatedOn(),
				bo.getEpisodeDocumentTypeId(),
				bo.getInternmentEpisodeId()
		);
	}

	private String buildPartialPath(Integer internmentEpisodeId, String relativeFilePath){
		log.debug("Input parameters -> internmentEpisodeId {}, relativeFilePath {}", internmentEpisodeId, relativeFilePath);
		String result = RELATIVE_DIRECTORY
				.replace("{internmentEpisodeId}", internmentEpisodeId.toString())
				.concat(relativeFilePath);
		log.debug(OUTPUT, result);
		return result;
	}
}
