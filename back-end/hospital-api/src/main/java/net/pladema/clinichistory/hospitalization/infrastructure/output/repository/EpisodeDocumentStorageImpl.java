package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.files.FileService;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocumentType;
import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.VEpisodeDocument;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import net.pladema.clinichistory.hospitalization.service.domain.SavedEpisodeDocumentResponseBo;

import net.pladema.clinichistory.hospitalization.service.domain.StoredFileBo;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EpisodeDocumentStorageImpl implements EpisodeDocumentStorage {

	private final SavedEpisodeDocumentRepository savedEpisodeDocumentRepository;

	private final EpisodeDocumentRepository episodeDocumentRepository;

	private final String OUTPUT = "Output -> {}";

	private final LocalDateMapper localDateMapper;

	private final InternmentEpisodeDocumentTypeRepository documentTypeRepository;

	private final FileService fileService;

	private static final String RELATIVE_DIRECTORY = "/internments/{internmentEpisodeId}/episodedocuments/";

	private final String PDF = ".pdf";

	public EpisodeDocumentStorageImpl(SavedEpisodeDocumentRepository savedEpisodeDocumentRepository, EpisodeDocumentRepository episodeDocumentRepository, LocalDateMapper localDateMapper, InternmentEpisodeDocumentTypeRepository documentTypeRepository, FileService fileService) {
		this.savedEpisodeDocumentRepository = savedEpisodeDocumentRepository;
		this.episodeDocumentRepository = episodeDocumentRepository;
		this.localDateMapper = localDateMapper;
		this.documentTypeRepository = documentTypeRepository;
		this.fileService = fileService;
	}

	@Override
	public SavedEpisodeDocumentResponseBo saveEpisodeDocument(EpisodeDocumentBo bo) {
		String newFileName = fileService.createFileName(FilenameUtils.getExtension(bo.getFile().getOriginalFilename()));
		String partialPath = buildPartialPath(bo.getInternmentEpisodeId(), newFileName);
		String uuid = newFileName.split("\\.")[0];
		try {
			fileService.transferMultipartFile(partialPath, uuid, "DOCUMENTO_EPISODIO", bo.getFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		EpisodeDocument ed = new EpisodeDocument(partialPath, StringUtils.cleanPath(bo.getFile().getOriginalFilename()), uuid, bo.getEpisodeDocumentTypeId(), bo.getInternmentEpisodeId());
		return this.mapToBo(this.savedEpisodeDocumentRepository.save(ed));
	}

	@Override
	public List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		List<EpisodeDocumentResponseBo> result = this.episodeDocumentRepository.findAll()
				.stream()
				.map(entity -> this.mapEntityToBo(entity))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<DocumentTypeBo> getDocumentTypes() {
		return this.documentTypeRepository.findAll()
				.stream()
				.map(entity -> this.mapDocumentTypeToBo(entity))
				.collect(Collectors.toList());
	}

	@Override
	public boolean deleteDocument(Integer episodeDocumentId) {
		Optional<EpisodeDocument> ed = this.savedEpisodeDocumentRepository.findById(episodeDocumentId);
		if ( ! ed.isPresent()) return false;

		String partialPath = buildPartialPath(ed.get().getInternmentEpisodeId(), ed.get().getUuidFile());
		this.savedEpisodeDocumentRepository.deleteById(episodeDocumentId);
		return fileService.deleteFile(partialPath + PDF);
	}

	@Override
	public StoredFileBo downloadEpisodeDocument(Integer episodeDocumentId) {
		Optional<EpisodeDocument> ed = this.savedEpisodeDocumentRepository.findById(episodeDocumentId);
		if ( ! ed.isPresent()) return new StoredFileBo();

		return new StoredFileBo(fileService.loadFileRelativePath(ed.get().getFilePath()));
	}

	private String buildPartialPath(Integer internmentEpisodeId, String relativeFilePath){
		log.debug("Input parameters -> internmentEpisodeId {}, relativeFilePath {}", internmentEpisodeId, relativeFilePath);
		String result = RELATIVE_DIRECTORY
				.replace("{internmentEpisodeId}", internmentEpisodeId.toString())
				.concat(relativeFilePath);
		log.debug(OUTPUT, result);
		return result;
	}

	private EpisodeDocumentResponseBo mapEntityToBo(VEpisodeDocument entity) {
		return new EpisodeDocumentResponseBo(
				entity.getId(),
				entity.getDescription(),
				entity.getFileName(),
				localDateMapper.toDateDto(entity.getCreatedOn())
		);
	}

	private DocumentTypeBo mapDocumentTypeToBo(EpisodeDocumentType entity) {
		return new DocumentTypeBo(entity.getId(), entity.getDescription());
	}

	SavedEpisodeDocumentResponseBo mapToBo(EpisodeDocument entity) {
		return new SavedEpisodeDocumentResponseBo(
				entity.getId(),
				entity.getFilePath(),
				entity.getFileName(),
				entity.getUuidFile(),
				localDateMapper.toDateDto(entity.getCreatedOn()),
				entity.getEpisodeDocumentTypeId(),
				entity.getInternmentEpisodeId()
		);
	}
}