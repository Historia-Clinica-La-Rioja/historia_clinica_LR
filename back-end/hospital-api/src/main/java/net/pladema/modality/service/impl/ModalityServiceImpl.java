package net.pladema.modality.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.repository.ModalityRepository;
import net.pladema.establishment.repository.entity.Modality;
import net.pladema.modality.service.ModalityService;
import net.pladema.modality.service.domain.ModalityBO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModalityServiceImpl implements ModalityService {

	private final ModalityRepository modalityRepository;

	@Override
	public List<ModalityBO> getAllModality() {
		List<Modality> modalities = modalityRepository.getAllModality();
		List<ModalityBO> result = modalities.stream().map(this::createModalityBoInstance).collect(Collectors.toList());
		return result;
	}

	@Override
	public List<ModalityBO> getModalitiesByStudiesCompleted(Integer institutionId) {
		log.debug("Get modalities with studies completed, Input parameters -> institutionId {}", institutionId);
		List<Modality> modalities = modalityRepository.getModalitiesByStudiesCompleted(institutionId);
		List<ModalityBO> result = modalities.stream().map(this::createModalityBoInstance).collect(Collectors.toList());
		log.debug("Output -> modalities with studies completed", result);
		return result;
	}

	public ModalityBO getModality(Integer modalityId) {
		Modality modality = modalityRepository.findById(modalityId).orElse(null);
		if (modality != null){
			return createModalityBoInstance(modality);
		}
		return null;
	}

	private ModalityBO createModalityBoInstance(Modality modality) {
		log.debug("Input parameters -> modality {}", modality);
		ModalityBO result = new ModalityBO();
		result.setId(modality.getId().shortValue());
		result.setAcronym(modality.getAcronym());
		result.setDescription(modality.getDescription());
		log.debug("Output -> ModalityBO{}", result);
		return result;
	}
}
