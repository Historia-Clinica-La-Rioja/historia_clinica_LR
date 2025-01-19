package net.pladema.violencereport.application;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.person.service.PersonService;
import net.pladema.violencereport.domain.ViolenceReportFilterBo;
import net.pladema.violencereport.domain.ViolenceReportSituationEvolutionBo;

import net.pladema.violencereport.infrastructure.output.repository.ViolenceReportRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class GetHistoricList {

	private ViolenceReportRepository violenceReportRepository;

	private PersonService personService;

	public List<ViolenceReportSituationEvolutionBo> run(Integer patientId, ViolenceReportFilterBo filter) {
		log.debug("Input parameters -> patientId {}, filter {}", patientId, filter);
		List<ViolenceReportSituationEvolutionBo> result = violenceReportRepository.getPatientHistoric(patientId, filter.getInstitutionId(), filter.getSituationId(), filter.getModalityId(), filter.getTypeId());
		getProfessionalsNames(result);
		log.debug("Output -> {}", result);
		return result;
	}

	private void getProfessionalsNames(List<ViolenceReportSituationEvolutionBo> result) {
		List<Integer> personIds = result.stream().map(ViolenceReportSituationEvolutionBo::getProfessionalPersonId).collect(Collectors.toList());
		personIds.forEach(personId -> parseProfessionalCompleteName(result, personId));
	}

	private void parseProfessionalCompleteName(List<ViolenceReportSituationEvolutionBo> result, Integer personId) {
		String personCompleteName = personService.getCompletePersonNameById(personId);
		result.stream().filter(partialResult -> partialResult.getProfessionalPersonId().equals(personId)).forEach(partialResult -> partialResult.setProfessionalFullName(personCompleteName));
	}

}
