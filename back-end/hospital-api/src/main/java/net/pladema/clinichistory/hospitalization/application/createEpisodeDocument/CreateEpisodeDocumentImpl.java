package net.pladema.clinichistory.hospitalization.application.createEpisodeDocument;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import ar.lamansys.sgx.shared.files.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;
import net.pladema.clinichistory.hospitalization.controller.dto.EpisodeDocumentDto;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

@Slf4j
@AllArgsConstructor
@Service
public class CreateEpisodeDocumentImpl implements CreateEpisodeDocument {
	private static final String RELATIVE_DIRECTORY = "/internments/{internmentEpisodeId}/episodedocuments/";
	private static final String OUTPUT = "Output -> {}";

	private final EpisodeDocumentStorage episodeDocumentStorage;
	private final FileService fileService;


	@Override
	public Integer run(EpisodeDocumentDto dto) {
		log.debug("Input parameters -> dto {}", dto);
		String newFileName = fileService.createFileName(FilenameUtils.getExtension(dto.getFile().getOriginalFilename()));

		var path = fileService.buildCompletePath(
				buildPartialPath(dto.getInternmentEpisodeId(), newFileName)
		);
		String uuid = newFileName.split("\\.")[0];
		var fileInfo = fileService.transferMultipartFile(path, uuid, "DOCUMENTO_DE_ENCUENTRO", dto.getFile());

		Integer result = episodeDocumentStorage.saveEpisodeDocument(
				path,
				uuid,
				new EpisodeDocumentBo(dto.getFile().getOriginalFilename(), dto.getEpisodeDocumentTypeId(), dto.getInternmentEpisodeId())
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
