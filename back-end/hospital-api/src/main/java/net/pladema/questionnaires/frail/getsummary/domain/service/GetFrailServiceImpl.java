package net.pladema.questionnaires.frail.getsummary.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.dto.QuestionnaireSummary;
import net.pladema.questionnaires.frail.create.repository.FrailRepository;
import net.pladema.questionnaires.frail.get.domain.service.GetFrailService;
import net.pladema.questionnaires.frail.getsummary.repository.FrailSummaryRepository;


@Service
public class GetFrailServiceImpl implements GetFrailService {

	private final FrailRepository frailRepository;

	private final FrailSummaryRepository questionnaireSummaryRepository;

	public GetFrailServiceImpl(FrailRepository frailRepository, FrailSummaryRepository questionnaireSummaryRepository) {
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
