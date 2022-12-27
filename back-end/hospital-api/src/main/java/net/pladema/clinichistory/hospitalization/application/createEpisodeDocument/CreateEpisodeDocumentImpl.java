package net.pladema.clinichistory.hospitalization.application.createEpisodeDocument;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CreateEpisodeDocumentImpl implements CreateEpisodeDocument {

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final FileService fileService;
	private static final String RELATIVE_DIRECTORY = "/internments/{internmentEpisodeId}/episodedocuments/";
	private final String OUTPUT = "Output -> {}";

	public CreateEpisodeDocumentImpl(EpisodeDocumentStorage episodeDocumentStorage, FileService fileService) {
		this.episodeDocumentStorage = episodeDocumentStorage;
		this.fileService = fileService;
	}

	@Override
	public Integer run(EpisodeDocumentDto dto) {
		log.debug("Input parameters -> dto {}", dto);
		String newFileName = fileService.createFileName(FilenameUtils.getExtension(dto.getFile().getOriginalFilename()));
		String partialPath = buildPartialPath(dto.getInternmentEpisodeId(), newFileName);
		String uuid = newFileName.split("\\.")[0];
		try {
			fileService.transferMultipartFile(partialPath, uuid, "DOCUMENTO_EPISODIO", dto.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Integer result = episodeDocumentStorage.saveEpisodeDocument(
				partialPath,
				uuid,
				new EpisodeDocumentBo(dto.getFile(), dto.getEpisodeDocumentTypeId(), dto.getInternmentEpisodeId())
		);
		log.debug(OUTPUT, result);
		return result;
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
