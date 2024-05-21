package net.pladema.emergencycare.triage.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.triage.application.ports.TriageReasonStorage;

import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import net.pladema.emergencycare.triage.infrastructure.output.repository.TriageReasonRepository;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TriageReasonStorageImpl implements TriageReasonStorage {

	private final TriageReasonRepository triageReasonRepository;

	@Override
	public TriageReason saveTriageReason(TriageReason triageReason) {
		return triageReasonRepository.save(triageReason);
	}
}
