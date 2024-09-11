package net.pladema.emergencycare.triage.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.application.port.output.EmergencyCareTriageCategoryStorage;
import net.pladema.emergencycare.triage.repository.TriageCategoryRepository;
import net.pladema.emergencycare.triage.service.domain.TriageCategoryBo;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmergencyCareTriageCategoryStorageImpl implements EmergencyCareTriageCategoryStorage {

	private final TriageCategoryRepository triageCategoryRepository;

	@Override
	public TriageCategoryBo getById(Short id) {
		return new TriageCategoryBo(triageCategoryRepository.getById(id));
	}
}
