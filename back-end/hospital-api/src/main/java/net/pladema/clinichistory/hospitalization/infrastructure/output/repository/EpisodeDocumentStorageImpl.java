package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EpisodeDocumentStorageImpl implements EpisodeDocumentStorage {

	private final EpisodeDocumentRepository episodeDocumentRepository;

	private final String OUTPUT = "Output -> {}";

	private final LocalDateMapper localDateMapper;

	public EpisodeDocumentStorageImpl(EpisodeDocumentRepository episodeDocumentRepository, LocalDateMapper localDateMapper) {
		this.episodeDocumentRepository = episodeDocumentRepository;
		this.localDateMapper = localDateMapper;
	}

	@Override
	public EpisodeDocumentResponseBo saveEpisodeDocument(EpisodeDocumentBo bo) {
		EpisodeDocument ed = new EpisodeDocument(bo.getFilePath(), bo.getFileName(), bo.getUuid(), bo.getEpisodeDocumentTypeId(), bo.getInternmentEpisodeId());
		return this.mapToBo(this.episodeDocumentRepository.save(ed));
	}

	@Override
	public List<EpisodeDocumentResponseBo> getEpisodeDocuments(Integer internmentEpisodeId) {
		log.debug("Input parameters -> internmentEpisodeId {}", internmentEpisodeId);
		List<EpisodeDocumentResponseBo> result = this.episodeDocumentRepository.findAll()
				.stream()
				.map(entity -> this.mapToBo(entity))
				.collect(Collectors.toList());
		log.debug(OUTPUT, result);
		return result;
	}

	EpisodeDocumentResponseBo mapToBo(EpisodeDocument entity) {
		return new EpisodeDocumentResponseBo(
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