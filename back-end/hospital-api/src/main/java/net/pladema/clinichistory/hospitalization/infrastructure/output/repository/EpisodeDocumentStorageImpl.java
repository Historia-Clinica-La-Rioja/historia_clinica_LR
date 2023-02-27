package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.files.FileService;
import ar.lamansys.sgx.shared.filestorage.application.FilePathBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentStorage;
import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;
import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.InternmentEpisodeDocumentType;
import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.VEpisodeDocument;
import net.pladema.clinichistory.hospitalization.service.domain.DocumentTypeBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;
import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

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
	public Integer saveEpisodeDocument(FilePathBo path, String uuid, EpisodeDocumentBo bo) {
		log.debug("Input parameters -> partialPath {}, uuid {}, bo {}", path.relativePath, uuid, bo);
		EpisodeDocument ed = new EpisodeDocument(path.relativePath, StringUtils.cleanPath(bo.getOriginalFilename()), uuid, bo.getEpisodeDocumentTypeId(), bo.getInternmentEpisodeId());
		EpisodeDocument entity = this.savedEpisodeDocumentRepository.save(ed);
		log.debug(OUTPUT, entity);
		return entity.getId();
	}

	@Override
	public List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		List<EpisodeDocumentResponseBo> result = this.episodeDocumentRepository.findAllByInternmentEpisodeId(internmentEpisodeId)
				.stream()
				.map(this::mapEntityToBo)
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public List<DocumentTypeBo> getDocumentTypes() {
		List<DocumentTypeBo> result = this.documentTypeRepository.findAll()
				.stream()
				.map(entity -> this.mapDocumentTypeToBo(entity))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public boolean deleteDocument(Integer episodeDocumentId) {
		log.debug("Input parameters -> episodeDocumentId {}", episodeDocumentId);
		Optional<EpisodeDocument> ed = this.savedEpisodeDocumentRepository.findById(episodeDocumentId);
		if ( ! ed.isPresent()) return false;

		this.savedEpisodeDocumentRepository.deleteById(episodeDocumentId);

		var path = fileService.buildCompletePath(ed.get().getFilePath());
		Boolean result = fileService.deleteFile(path);
		log.debug(OUTPUT, result);
		return result;
	}

	@Override
	public StoredFileBo downloadEpisodeDocument(Integer episodeDocumentId) {
		log.debug("Input parameters -> episodeDocumentId {}", episodeDocumentId);
		Optional<EpisodeDocument> ed = this.savedEpisodeDocumentRepository.findById(episodeDocumentId);
		if ( ! ed.isPresent()) return null;
		var episodeDocument = ed.get();
		var path = fileService.buildCompletePath(episodeDocument.getFilePath());
		var bo = new StoredFileBo(
				fileService.loadFile(path),
				MediaType.APPLICATION_PDF.toString(),
				episodeDocument.getFileName()
		);
		log.debug(OUTPUT, bo);
		return bo;
	}

	private EpisodeDocumentResponseBo mapEntityToBo(VEpisodeDocument entity) {
		return new EpisodeDocumentResponseBo(
				entity.getId(),
				entity.getDescription(),
				entity.getFileName(),
				localDateMapper.toDateDto(entity.getCreatedOn())
		);
	}

	private DocumentTypeBo mapDocumentTypeToBo(InternmentEpisodeDocumentType entity) {
		return new DocumentTypeBo(entity.getId(), entity.getDescription());
	}
}