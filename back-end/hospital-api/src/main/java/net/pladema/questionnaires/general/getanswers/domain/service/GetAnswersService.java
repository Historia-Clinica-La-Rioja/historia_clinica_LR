package net.pladema.questionnaires.general.getanswers.domain.service;

import net.pladema.questionnaires.common.repository.entity.QuestionnaireResponse;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;

import net.pladema.questionnaires.common.dto.AnswerDTO;
import net.pladema.questionnaires.common.repository.entity.Answer;
import net.pladema.questionnaires.common.repository.AnswerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetAnswersService {

	private final QuestionnaireResponseRepository responseRepository;

	private final AnswerRepository answerRepository;

	@Autowired
    public GetAnswersService(QuestionnaireResponseRepository responseRepository, AnswerRepository answerRepository) {
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
    }

	public List<AnswerDTO> getAnswersForResponse(Integer responseId) {

		Optional<QuestionnaireResponse> responseOptional = responseRepository.findById(responseId);

		if (responseOptional.isPresent()) {
			List<Answer> answers = answerRepository.findByQuestionnaireResponseId(responseId);

            return answers.stream()
					.map(answer -> {
						AnswerDTO dto = new AnswerDTO();
						dto.setItemId(answer.getItemId());
						dto.setValue(answer.getValue());
						dto.setOptionId(answer.getAnswerId());
						return dto;
					})
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

}
