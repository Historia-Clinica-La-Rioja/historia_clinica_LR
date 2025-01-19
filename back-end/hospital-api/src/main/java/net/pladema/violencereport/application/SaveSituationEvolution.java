package net.pladema.violencereport.application;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.violencereport.domain.ViolenceReportAggressorBo;
import net.pladema.violencereport.domain.ViolenceReportBo;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportAggressorRepository;
import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

@Slf4j
@AllArgsConstructor
@Service
public class SaveSituationEvolution {

	private ViolenceReportRepository violenceReportRepository;

	private SaveViolenceReport saveViolenceReport;

	private ViolenceReportAggressorRepository violenceReportAggressorRepository;

	public Integer run(ViolenceReportBo violenceReport) {
		log.debug("Input parameters -> violenceReport {}", violenceReport);
		Short evolutionId = violenceReportRepository.getSituationLastEvolutionId(violenceReport.getPatientId(), violenceReport.getSituationId());
		setViolenceReportEvolutionId(violenceReport, evolutionId);
		assertValidAggressors(violenceReport);
		Integer result = saveViolenceReport.run(violenceReport);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertValidAggressors(ViolenceReportBo violenceReport) {
		List<ViolenceReportAggressorBo> oldAggressors = violenceReportAggressorRepository.getAllFromLastEvolution(violenceReport.getPatientId(), violenceReport.getSituationId(), (short) (violenceReport.getEvolutionId() - 1));
		HashSet<ViolenceReportAggressorBo> newAggressors = new HashSet<>(violenceReport.getAggressors());
		Assert.isTrue(newAggressors.containsAll(oldAggressors), "Hay agresores faltantes de evoluciones anteriores en la nueva");
	}

	private void setViolenceReportEvolutionId(ViolenceReportBo violenceReport, Short evolutionId) {
		Assert.notNull(evolutionId, "No se puede evolucionar un episodio inexistente");
		violenceReport.setEvolutionId((short) (evolutionId + 1));
	}

}
