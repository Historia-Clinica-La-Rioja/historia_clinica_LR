package net.pladema.questionnaires.general.create.domain.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.questionnaires.common.dto.AnswerDTO;
import net.pladema.questionnaires.common.dto.QuestionnaireResponseDTO;
import net.pladema.questionnaires.common.repository.entity.Answer;
import net.pladema.questionnaires.common.repository.AnswerRepository;

@Service
public class CreateQuestionnaireService {

	private static final Logger logger = LoggerFactory.getLogger(CreateQuestionnaireService.class);

	@Autowired
	private QuestionnaireResponseRepository responseRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Transactional
	public QuestionnaireResponse createQuestionnaireResponse (QuestionnaireResponseDTO responseDTO, Integer patientId) {
		logger.info("Starting transaction to create questionnaire response for patientId: {}", patientId);

		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setQuestionnaireId(responseDTO.getQuestionnaireId());
		questionnaireResponse.setPatientId(patientId);

		questionnaireResponse.setStatusId(2);

		logger.debug("Saving questionnaire response: {}", questionnaireResponse);
		responseRepository.save(questionnaireResponse);

		for (AnswerDTO answerDTO : responseDTO.getAnswers()) {
			Answer answer = new Answer();
			answer.setItemId(answerDTO.getItemId());
			answer.setValue(answerDTO.getValue());
			if (answerDTO.getOptionId() != null && answerDTO.getOptionId() == 0) {
				answer.setAnswerId(null);
			} else {
				answer.setAnswerId(answerDTO.getOptionId());
			}

			answer.setQuestionnaireResponse(questionnaireResponse);

			logger.debug("Saving answer: {}", answer);
			answerRepository.save(answer);
		}

		logger.info("Finished transaction to create questionnaire response for patientId: {}", patientId);
		return questionnaireResponse;
    }
}
