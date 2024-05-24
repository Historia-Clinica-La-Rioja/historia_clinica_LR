package net.pladema.emergencycare.triage.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.emergencycare.triage.application.ports.TriageReasonStorage;

import net.pladema.emergencycare.triage.domain.TriageReasonBo;
import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import net.pladema.emergencycare.triage.infrastructure.output.repository.TriageReasonRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TriageReasonStorageImpl implements TriageReasonStorage {

	private final TriageReasonRepository triageReasonRepository;

	@Override
	public List<TriageReasonBo> saveTriageReasons(List<TriageReasonBo> triageReasonsBo) {
		triageReasonRepository.saveAll(triageReasonsBo.stream()
				.map(t -> new TriageReason(t.getTriageId(), t.getReasonId()))
				.collect(Collectors.toList()));
		return triageReasonsBo;
	}

	@Override
	public List<TriageReasonBo> getAllByTriageId(Integer triageId) {
		return triageReasonRepository.findAllByPkTriageId(triageId)
				.stream().map(t -> new TriageReasonBo(t.getPk().getTriageId(),t.getPk().getReasonId()))
				.collect(Collectors.toList());
	}
}
