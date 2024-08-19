package net.pladema.emergencycare.triage.application.addtriagereasons;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;


import net.pladema.emergencycare.triage.application.ports.TriageReasonStorage;
import net.pladema.emergencycare.triage.domain.TriageReasonBo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AddTriageReasons {

	private final ReasonPort reasonPort;
	private final TriageReasonStorage triageReasonStorage;

	@Transactional
	public List<ReasonBo> run(Integer triageId, List<ReasonBo> reasons){
		log.debug("Input parameters -> triageId {}, reasons {}", triageId, reasons);
		reasonPort.saveReasons(reasons);
		triageReasonStorage.saveTriageReasons(reasons.stream()
						.map(r -> new TriageReasonBo(triageId, r.getSctid()))
						.collect(Collectors.toList())
		);
		log.debug("Output -> {}", reasons);
		return reasons;
	}

}
