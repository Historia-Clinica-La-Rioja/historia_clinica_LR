package net.pladema.questionnaires.frail.getsummary.domain.service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.domain.service.GetQuestionnaireService;

import net.pladema.questionnaires.common.dto.QuestionnaireSummary;
import net.pladema.questionnaires.common.repository.QuestionnaireSummaryRepository;
import net.pladema.questionnaires.frail.create.repository.FrailRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class GetFrailServiceImpl implements GetQuestionnaireService {

	private final FrailRepository frailRepository;

	private final QuestionnaireSummaryRepository questionnaireSummaryRepository;

	public GetFrailServiceImpl(FrailRepository frailRepository, QuestionnaireSummaryRepository questionnaireSummaryRepository) {
		this.frailRepository = frailRepository;
		this.questionnaireSummaryRepository = questionnaireSummaryRepository;
	}

	public List<Answer> findPatientQuestionnaire(Integer patientId) {
		return this.frailRepository.findPatientFrailTest(patientId);
	}

	public Optional<QuestionnaireSummary> findQuestionnaireSummary(Integer questionnaireId) {
		return this.questionnaireSummaryRepository.getQuestionnaireSummaryReport(questionnaireId);
	}
}
