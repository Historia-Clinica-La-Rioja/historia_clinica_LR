package net.pladema.emergencycare.triage.application.fetchtriagereasons;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;
import net.pladema.emergencycare.triage.application.ports.TriageReasonStorage;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FetchTriageReasons {

	private final ReasonPort reasonPort;
	private final TriageReasonStorage triageReasonStorage;

	public List<ReasonBo> run(Integer triageId){
		log.debug("Input parameters -> triageId {} ", triageId);
		List<ReasonBo> result = triageReasonStorage.getAllByTriageId(triageId)
				.stream().map(tr -> reasonPort.getByReasonId(tr.getPk().getReasonId()).orElse(null))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		log.debug("Output -> {}", result);
		return result;
	}

}
