package net.pladema.questionnaires.edmonton.getsummary.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;
import net.pladema.questionnaires.edmonton.create.repository.EdmontonRepository;
import net.pladema.questionnaires.edmonton.get.domain.service.GetEdmontonService;
import net.pladema.questionnaires.edmonton.getsummary.repository.EdmontonSummaryRepository;

@Service
public class GetEdmontonServiceImpl implements GetEdmontonService {
	private final EdmontonRepository edmontonRepository;

	private final EdmontonSummaryRepository questionnaireSummaryRepository;

	public GetEdmontonServiceImpl(EdmontonRepository edmontonRepository, EdmontonSummaryRepository questionnaireSummaryRepository) {
		this.edmontonRepository = edmontonRepository;
		this.questionnaireSummaryRepository = questionnaireSummaryRepository;
	}

	public List<Answer> findPatientQuestionnaire(Integer patientId) {
		return this.edmontonRepository.findPatientEdmontonTest(patientId);
	}

	public Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId) {
		return this.questionnaireSummaryRepository.getQuestionnaireSummaryReport(questionnaireId);
	}
}
