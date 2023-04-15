package net.pladema.emergencycare.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.ReasonBo;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.outpatient.createoutpatient.service.outpatientReason.ReasonPort;
import net.pladema.emergencycare.repository.EmergencyCareEvolutionNoteReasonRepository;
import net.pladema.emergencycare.repository.entity.EmergencyCareEvolutionNoteReason;
import net.pladema.emergencycare.service.EmergencyCareEvolutionNoteReasonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class EmergencyCareEvolutionNoteReasonServiceImpl implements EmergencyCareEvolutionNoteReasonService {

	private static final Logger LOG = LoggerFactory.getLogger(EmergencyCareEvolutionNoteReasonServiceImpl.class);

	public static final String OUTPUT = "Output -> {}";

	private final EmergencyCareEvolutionNoteReasonRepository emergencyCareEvolutionNoteReasonRepository;

	private final ReasonPort reasonPort;

	@Override
	public List<ReasonBo> addReasons(Integer emergencyCareEvolutionNoteId, List<ReasonBo> reasons) {
		LOG.debug("Input parameters -> emergencyCareEvolutionNoteId {}, reasons {}", emergencyCareEvolutionNoteId, reasons);
		reasons.stream()
				.map(reasonPort::saveReason)
				.forEach(r -> saveEmergencyCareEvolutionNoteReason(r, emergencyCareEvolutionNoteId));
		LOG.debug(OUTPUT, reasons);
		return reasons;
	}

	private EmergencyCareEvolutionNoteReason saveEmergencyCareEvolutionNoteReason(ReasonBo reason, Integer emergencyCareEvolutionNoteId) {
		LOG.debug("Input parameters reason {}, emergencyCareEvolutionNoteId {}", reason, emergencyCareEvolutionNoteId);
		Objects.requireNonNull(reason);
		EmergencyCareEvolutionNoteReason result =
				emergencyCareEvolutionNoteReasonRepository.save(new EmergencyCareEvolutionNoteReason(emergencyCareEvolutionNoteId, reason.getSctid()));
		LOG.debug(OUTPUT, result);
		return result;
	}

}
