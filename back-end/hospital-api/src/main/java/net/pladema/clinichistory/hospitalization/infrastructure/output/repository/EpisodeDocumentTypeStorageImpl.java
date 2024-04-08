package net.pladema.clinichistory.hospitalization.infrastructure.output.repository;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.hospitalization.application.port.EpisodeDocumentTypeStorage;

import net.pladema.clinichistory.hospitalization.service.domain.EpisodeDocumentTypeBo;

import net.pladema.establishment.repository.EpisodeDocumentTypeRepository;

import net.pladema.staff.repository.entity.EpisodeDocumentType;

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

	@Override
	public EpisodeDocumentTypeBo getEpisodeDocumentTypeById(Integer id) {
		log.debug("Input parameters -> id {}", id);
		EpisodeDocumentTypeBo result;
		if (id != EpisodeDocumentType.REGULAR && episodeDocumentTypeRepository.existsConsentDocumentById(id))
			result = new EpisodeDocumentTypeBo(episodeDocumentTypeRepository.getConsentDocumentTypeById(id));
		else
			throw new NotFoundException("consent-not-exists", "El tipo de consentimiento no existe");
		log.debug("Output -> {}", result);
		return result;
	}
}
