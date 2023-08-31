package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentTypeStorage;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import net.pladema.establishment.repository.EpisodeDocumentTypeRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EpisodeDocumentTypeStorageImpl implements EpisodeDocumentTypeStorage {

	private final EpisodeDocumentTypeRepository episodeDocumentTypeRepository;

	@Override
	public List<EpisodeDocumentTypeBo> getEpisodeDocumentType() {
		List<EpisodeDocumentTypeBo> result = episodeDocumentTypeRepository.getConsentDocuments()
				.stream()
				.map(entity -> new EpisodeDocumentTypeBo(entity))
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}
}
