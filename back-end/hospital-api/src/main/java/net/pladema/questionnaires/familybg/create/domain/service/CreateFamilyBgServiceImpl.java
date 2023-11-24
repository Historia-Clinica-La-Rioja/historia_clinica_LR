package net.pladema.questionnaires.familybg.create.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.Answer;
import net.pladema.questionnaires.common.domain.QuestionnaireResponse;
import net.pladema.questionnaires.common.domain.model.QuestionnaireAnswerBO;
import net.pladema.questionnaires.common.domain.model.QuestionnaireBO;
import net.pladema.questionnaires.familybg.create.repository.FamilyBgRepository;
import net.pladema.questionnaires.familybg.get.domain.service.GetFamilyBgService;

@Service
public class CreateFamilyBgServiceImpl implements CreateFamilyBgService {

	private static final Logger logger = LoggerFactory.getLogger(CreateFamilyBgServiceImpl.class);

	private final FamilyBgRepository familyBgRepository;

	private final GetFamilyBgService getQuestionnaireService;

	public CreateFamilyBgServiceImpl(FamilyBgRepository familyBgRepository, GetFamilyBgService getQuestionnaireService) {
		this.familyBgRepository = familyBgRepository;
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
				QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse(familyBgRepository.findById(id));
				familyBgRepository.updateStatusById(questionnaireResponse.getId(), 3);
			}

			QuestionnaireResponse entity = createQuestionnaireTest(questionnaireBO);

			familyBgRepository.save(entity);
			logger.info("Family background questionnaire created and saved for patient with id {}", patientId);

		}
		logger.info("Execution of the 'execute' method completed.");
		return questionnaireBO;
	}

	private QuestionnaireResponse createQuestionnaireTest(QuestionnaireBO questionnaireBO) {
		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setPatientId(questionnaireBO.getPatientId());
		if (questionnaireBO.getAnswers() != null && !questionnaireBO.getAnswers().isEmpty()) {
			questionnaireResponse.setAnswers(new ArrayList<>());

			for (QuestionnaireAnswerBO answerBO : questionnaireBO.getAnswers()) {
				Answer answer = new Answer();
				if (answerBO.getAnswerId() == 0) {
					answer.setAnswerId(null);
				} else {
					answer.setAnswerId(Integer.valueOf(answerBO.getAnswerId()));
				}
				answer.setItemId(Integer.valueOf(answerBO.getQuestionId()));
				answer.setValue(String.valueOf(answerBO.getValue()));

				questionnaireResponse.getAnswers().add(answer);
			}

			questionnaireResponse.setQuestionnaireId(2);
			questionnaireResponse.setStatusId(2);
		}

		return questionnaireResponse;
	}

}
