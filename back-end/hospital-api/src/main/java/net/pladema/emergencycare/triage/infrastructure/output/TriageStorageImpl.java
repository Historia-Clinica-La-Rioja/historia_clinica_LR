package net.pladema.emergencycare.triage.infrastructure.output;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.repository.domain.ProfessionalPersonVo;
import net.pladema.emergencycare.triage.application.ports.TriageStorage;
import net.pladema.emergencycare.triage.domain.TriageBo;

import net.pladema.emergencycare.triage.infrastructure.output.repository.TriageRepository;

import net.pladema.medicalconsultation.diary.service.domain.ProfessionalPersonBo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TriageStorageImpl implements TriageStorage {

	private final TriageRepository triageRepository;

	@Override
	public Optional<TriageBo> getLatestByEmergencyCareEpisodeId(Integer emergencyCareEpisodeId) {
		return triageRepository.findAllByEmergencyCareEpisodeIdOrderByIdDesc(emergencyCareEpisodeId)
				.stream().findFirst()
				.map(TriageBo::new);
	}

	@Override
	public Optional<ProfessionalPersonBo> getTriageRelatedProfessionalInfo(Integer id){
		log.debug("Input parameters -> id {}", id);
		var professionalPersonVo = triageRepository.getTriageRelatedProfessionalInfo(id);
		if (professionalPersonVo != null)
			return Optional.of(new ProfessionalPersonBo(professionalPersonVo));
		return Optional.empty();
	}

}
