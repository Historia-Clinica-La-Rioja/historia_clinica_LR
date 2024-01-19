package net.pladema.questionnaires.general.create.domain.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.questionnaires.general.create.domain.dto.AnswerDTO;
import net.pladema.questionnaires.general.create.domain.dto.QuestionnaireResponseDTO;
import net.pladema.questionnaires.general.create.repository.entity.AnswerII;
import net.pladema.questionnaires.general.create.repository.entity.AnswerRepositoryII;

@Service
public class CreateQuestionnaireService {

	@Autowired
	private QuestionnaireResponseRepository responseRepository;

	@Autowired
	private AnswerRepositoryII answerRepository;

	@Transactional
	public QuestionnaireResponseII createQuestionnaireResponse (QuestionnaireResponseDTO responseDTO, Integer patientId) {

		QuestionnaireResponseII questionnaireResponse = new QuestionnaireResponseII();
		questionnaireResponse.setQuestionnaireId(responseDTO.getQuestionnaireId());
		questionnaireResponse.setPatientId(patientId);

		questionnaireResponse.setStatusId(2);

		responseRepository.save(questionnaireResponse);

		for (AnswerDTO answerDTO : responseDTO.getAnswers()) {
			AnswerII answer = new AnswerII();
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
