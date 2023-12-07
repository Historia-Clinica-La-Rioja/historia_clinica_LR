package net.pladema.questionnaires.edmonton.create.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.domain.QuestionnaireResponse;
import net.pladema.questionnaires.common.domain.model.QuestionnaireAnswerBO;
import net.pladema.questionnaires.common.domain.model.QuestionnaireBO;
import net.pladema.questionnaires.edmonton.create.repository.EdmontonRepository;
import net.pladema.questionnaires.edmonton.get.domain.service.GetEdmontonService;

@Service
public class CreateEdmontonServiceImpl implements CreateEdmontonService {

	private static final Logger logger = LoggerFactory.getLogger(CreateEdmontonServiceImpl.class);

	private final EdmontonRepository edmontonRepository;

	private final GetEdmontonService getQuestionnaireService;

	public CreateEdmontonServiceImpl(EdmontonRepository edmontonRepository, GetEdmontonService getQuestionnaireService) {
		this.edmontonRepository = edmontonRepository;
		this.getQuestionnaireService = getQuestionnaireService;
	}

	@Override
	public QuestionnaireBO execute(QuestionnaireBO questionnaireBO) {

		logger.info("Executing the 'execute' method...");

		Integer patientId = questionnaireBO.getPatientId();

		List<Answer> lst = getQuestionnaireService.findPatientQuestionnaire(patientId);

		if (lst != null) {
			for (Answer answer : lst) {
				Integer id = answer.getQuestionnaireResponseId();
				logger.debug("Processing answer with id {}", id);
				QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse(edmontonRepository.findById(id));
				edmontonRepository.updateStatusById(questionnaireResponse.getId(), 3);
			}

			QuestionnaireResponse entity = createQuestionnaireTest(questionnaireBO);

			edmontonRepository.save(entity);
			logger.info("Edmonton questionnaire created and saved for patient with id {}", patientId);
		}
		logger.info("Execution of the 'execute' method completed.");
		return questionnaireBO;
	}

	private QuestionnaireResponse createQuestionnaireTest(QuestionnaireBO questionnaireBO) {
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		QuestionnaireAnswerBO answerBO;
		Answer answer;
		Integer questionnaireId = 2;
		Integer statusId = 2;

		questionnaireResponse.setPatientId(questionnaireBO.getPatientId());

		if (questionnaireBO.getAnswers() != null && !questionnaireBO.getAnswers().isEmpty()) {
			questionnaireResponse.setAnswers(new ArrayList<>());

            for (QuestionnaireAnswerBO questionnaireAnswerBO : questionnaireBO.getAnswers()) {
                answerBO = questionnaireAnswerBO;
                answer = new Answer();
                answer.setAnswerId(Integer.valueOf(answerBO.getAnswerId()));
                answer.setItemId(Integer.valueOf(answerBO.getQuestionId()));
                answer.setValue(String.valueOf(answerBO.getValue()));
                questionnaireResponse.getAnswers().add(answer);
            }
			questionnaireResponse.setQuestionnaireId(questionnaireId);
			questionnaireResponse.setStatusId(statusId);
		}
		return questionnaireResponse;
	}
}
