package net.pladema.violencereport.application;

import java.util.List;

import io.jsonwebtoken.lang.Assert;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.cipres.domain.SnomedBo;
import net.pladema.violencereport.domain.ViolenceEpisodeDetailBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceModalityRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceTypeRepository;

@Slf4j
@AllArgsConstructor
@Service
public class GetSituationEvolution {

	private ViolenceReportRepository violenceReportRepository;

	private GetEditViolenceReportById getEditViolenceReportById;

	private ViolenceModalityRepository violenceModalityRepository;

	private ViolenceTypeRepository violenceTypeRepository;

	public ViolenceReportBo run(Integer patientId, Short situationId, Short evolutionId) {
		log.debug("Input parameters -> patientId {}, situationId {}, evolutionId {}", patientId, situationId, evolutionId);
		Integer reportId = violenceReportRepository.getReportIdByPatientIdAndSituationIdAndEvolutionId(patientId, situationId, evolutionId);
		assertNonNullReportId(reportId);
		ViolenceReportBo result = getViolenceReportBo(patientId, situationId, evolutionId, reportId);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertNonNullReportId(Integer reportId) {
		Assert.notNull(reportId, "No pueden obtenerse los datos de situaciones inexistentes");
	}

	private ViolenceReportBo getViolenceReportBo(Integer patientId, Short situationId, Short evolutionId, Integer reportId) {
		ViolenceReportBo result = getEditViolenceReportById.run(reportId);
		result.setEpisodeData(getEpisodeData(reportId));
		result.setEvolutionId(evolutionId);
		result.setSituationId(situationId);
		result.setPatientId(patientId);
		return result;
	}

	private ViolenceEpisodeDetailBo getEpisodeData(Integer reportId) {
		ViolenceEpisodeDetailBo episode = violenceReportRepository.getEpisodeDataByReportId(reportId);
		episode.setViolenceModalitySnomedList(getViolenceModalities(reportId));
		episode.setViolenceTypeSnomedList(getViolenceTypes(reportId));
		return episode;
	}

	private List<SnomedBo> getViolenceModalities(Integer reportId) {
		return violenceModalityRepository.getViolenceReportSnomedsByReportId(reportId);
	}

	private List<SnomedBo> getViolenceTypes(Integer reportId) {
		return violenceTypeRepository.getViolenceReportSnomedsByReportId(reportId);
	}

}
