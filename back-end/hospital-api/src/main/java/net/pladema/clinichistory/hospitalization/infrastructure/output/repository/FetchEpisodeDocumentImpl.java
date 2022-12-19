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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
