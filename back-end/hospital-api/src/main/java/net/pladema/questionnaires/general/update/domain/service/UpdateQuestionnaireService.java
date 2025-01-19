package net.pladema.questionnaires.general.update.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import net.pladema.questionnaires.common.dto.AnswerDTO;
import net.pladema.questionnaires.common.dto.UpdateResponseDTO;
import net.pladema.questionnaires.common.repository.AnswerRepository;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;
import net.pladema.questionnaires.common.repository.entity.Answer;
import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;

@Service
public class UpdateQuestionnaireService {

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionnaireResponseRepository questionnaireResponseRepository;

	public void updateAnswersAndQuestionnaireData (UpdateResponseDTO responseDTO, Integer questionnaireResponseId) throws NotFoundException {

		List<AnswerDTO> answerUpdates = responseDTO.getAnswers();

		QuestionnaireResponse response = questionnaireResponseRepository.findById(questionnaireResponseId)
				.orElseThrow(() -> new NotFoundException("Couldn't find questionnaire response with id " + questionnaireResponseId));

		List<Answer> existingAnswers = answerRepository.findByQuestionnaireResponseId(questionnaireResponseId);

		for (AnswerDTO answerDTO : answerUpdates) {
			boolean answerFound = false;
			for (Answer existingAnswer : existingAnswers) {
				if (existingAnswer.getItemId().equals(answerDTO.getItemId())) {
					if (answerDTO.getOptionId() != null && answerDTO.getOptionId() == 0) {
						existingAnswer.setAnswerId(null);
					} else {
						existingAnswer.setAnswerId(answerDTO.getOptionId());
					}
					existingAnswer.setValue(answerDTO.getValue());
					existingAnswer.setQuestionnaireResponse(response);
					answerRepository.save(existingAnswer);
					answerFound = true;
					break;
				}
			}
			if (!answerFound) {
				Answer newAnswer = new Answer();
				newAnswer.setItemId(answerDTO.getItemId());
				newAnswer.setAnswerId(answerDTO.getOptionId());
				newAnswer.setValue(answerDTO.getValue());
				newAnswer.setQuestionnaireResponse(response);
				answerRepository.save(newAnswer);
			}
		}

		response.setUpdatedOn(LocalDateTime.now());
		questionnaireResponseRepository.save(response);

	}
}
