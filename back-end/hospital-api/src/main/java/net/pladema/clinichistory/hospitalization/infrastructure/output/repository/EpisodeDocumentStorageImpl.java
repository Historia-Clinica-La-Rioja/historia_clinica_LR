package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.infrastructure.output.entities.EpisodeDocument;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentBo;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentResponseBo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class EpisodeDocumentStorageImpl implements EpisodeDocumentStorage {

	private final EpisodeDocumentRepository episodeDocumentRepository;

	public EpisodeDocumentStorageImpl(EpisodeDocumentRepository episodeDocumentRepository) {
		this.episodeDocumentRepository = episodeDocumentRepository;
	}

	@Override
	public EpisodeDocumentResponseBo saveEpisodeDocument(EpisodeDocumentBo bo) {
		EpisodeDocument ed = new EpisodeDocument(bo.getFilePath(), bo.getFileName(), bo.getUuid(), bo.getEpisodeDocumentTypeId(), bo.getInternmentEpisodeId());
		return this.mapToBo(this.episodeDocumentRepository.save(ed));
	}

	EpisodeDocumentResponseBo mapToBo(EpisodeDocument entity) {
		return new EpisodeDocumentResponseBo(
				entity.getId(),
				entity.getFilePath(),
				entity.getFileName(),
				entity.getUuidFile(),
				entity.getCreatedOn(),
				entity.getEpisodeDocumentTypeId(),
				entity.getInternmentEpisodeId()
		);
	}
}