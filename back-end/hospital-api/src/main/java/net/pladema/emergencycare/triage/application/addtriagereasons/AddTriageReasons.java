package net.pladema.emergencycare.triage.application.addtriagereasons;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;


import net.pladema.emergencycare.triage.application.ports.TriageReasonStorage;
import net.pladema.emergencycare.triage.infrastructure.output.entity.TriageReason;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddTriageReasons {

	private final ReasonPort reasonPort;
	private final TriageReasonStorage triageReasonStorage;

	public List<ReasonBo> run(Integer triageId, List<ReasonBo> reasons){
		log.debug("Input parameters -> triageId {}, reasons {}", triageId, reasons);
		reasons.stream()
				.map(reasonPort::saveReason)
				.forEach(r -> saveTriageReason(r, triageId));
		log.debug("Output -> {}", reasons);
		return reasons;
	}

	private TriageReason saveTriageReason(ReasonBo reason, Integer triageId) {
		log.debug("Input parameters -> triageId {}, reason {}", triageId, reason);
		Objects.requireNonNull(reason);
		TriageReason result = triageReasonStorage.saveTriageReason(
				new TriageReason(triageId,reason.getSctid())
		);
		log.debug("Output -> {}", result);
		return result;
	}
}
