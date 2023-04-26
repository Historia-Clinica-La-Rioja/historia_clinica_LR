package net.pladema.modality.service;

import net.pladema.modality.service.domain.ModalityBO;

import java.util.List;

public interface ModalityService {

	List<ModalityBO> getAllModality();

	List<ModalityBO> getModalitiesByStudiesCompleted(Integer institutionId);

	ModalityBO getModality(Integer modalityId);
}
