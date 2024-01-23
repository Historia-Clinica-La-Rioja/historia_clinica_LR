package net.pladema.questionnaires.general.create.domain.service;

import javax.transaction.Transactional;

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

	@Autowired
	private QuestionnaireResponseRepository responseRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Transactional
	public QuestionnaireResponse createQuestionnaireResponse (QuestionnaireResponseDTO responseDTO, Integer patientId) {

		QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();
		questionnaireResponse.setQuestionnaireId(responseDTO.getQuestionnaireId());
		questionnaireResponse.setPatientId(patientId);

		questionnaireResponse.setStatusId(2);

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

			answerRepository.save(answer);
		}

		return questionnaireResponse;
    }
}
