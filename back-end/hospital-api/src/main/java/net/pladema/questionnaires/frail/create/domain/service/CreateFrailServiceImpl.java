package net.pladema.questionnaires.frail.create.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.domain.QuestionnaireResponse;
import net.pladema.questionnaires.common.domain.model.QuestionnaireAnswerBO;
import net.pladema.questionnaires.common.domain.model.QuestionnaireBO;
import net.pladema.questionnaires.common.dto.QuestionnaireDTO;
import net.pladema.questionnaires.frail.create.repository.FrailRepository;
import net.pladema.questionnaires.frail.get.domain.service.GetFrailService;

@Service
public class CreateFrailServiceImpl implements CreateFrailService {

	private static final Logger logger = LoggerFactory.getLogger(CreateFrailServiceImpl.class);

	private final FrailRepository frailRepository;

	private final GetFrailService getQuestionnaireService;

	public CreateFrailServiceImpl(FrailRepository frailRepository, GetFrailService getQuestionnaireService) {
		this.frailRepository = frailRepository;
		this.getQuestionnaireService = getQuestionnaireService;
	}

	@Override
	public QuestionnaireBO execute(QuestionnaireBO questionnaireBO) {
		logger.info("Executing the 'execute' method...");

		QuestionnaireDTO questionnaireDTO = new QuestionnaireDTO();

		Integer patientId = questionnaireBO.getPatientId();

		List<Answer> lst = getQuestionnaireService.findPatientQuestionnaire(patientId);

		if (lst != null) {
			for (Answer answer : lst) {
				Integer id = answer.getQuestionnaireResponseId();
				logger.debug("Processing answer with id {}", id);
				QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse(frailRepository.findById(id));
				frailRepository.updateStatusById(questionnaireResponse.getId(), 3);
			}

			QuestionnaireResponse entity = createQuestionnaireTest(questionnaireBO);

			frailRepository.save(entity);
			logger.info("Frail questionnaire created and saved for patient with id {}", patientId);
		}
		logger.info("Execution of the 'execute' method completed.");
		return questionnaireBO;
	}

	private QuestionnaireResponse createQuestionnaireTest(QuestionnaireBO questionnaireBO) {
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		QuestionnaireAnswerBO answerBO;
		Answer answer;
		Integer questionnaireId = 3;
		Integer statusId = 2;
		int currentQuestionId = 60;

		questionnaireResponse.setPatientId(Math.toIntExact(questionnaireBO.getPatientId()));

		if (questionnaireBO.getAnswers() != null && !questionnaireBO.getAnswers().isEmpty()) {
			questionnaireResponse.setAnswers(new ArrayList<>());
			for (QuestionnaireAnswerBO questionnaireAnswerBO : questionnaireBO.getAnswers()) {
				answerBO = questionnaireAnswerBO;
				answer = new Answer();
				answer.setAnswerId(Math.toIntExact(answerBO.getAnswerId()));
				answer.setItemId(currentQuestionId);
				answer.setValue(String.valueOf(answerBO.getValue()));
				questionnaireResponse.getAnswers().add(answer);
				currentQuestionId += 2;
			}
			questionnaireResponse.setQuestionnaireId(questionnaireId);
			questionnaireResponse.setStatusId(statusId);
		}
		return questionnaireResponse;
	}
}
