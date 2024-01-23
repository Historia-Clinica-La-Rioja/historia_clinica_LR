package net.pladema.questionnaires.general.getanswers.domain.service;

import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;
import net.pladema.questionnaires.common.repository.QuestionnaireResponseRepository;

import net.pladema.questionnaires.general.create.domain.dto.AnswerDTO;
import net.pladema.questionnaires.general.create.repository.entity.AnswerII;
import net.pladema.questionnaires.general.create.repository.entity.AnswerRepositoryII;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetAnswersService {

	private final QuestionnaireResponseRepository responseRepository;

	private final AnswerRepositoryII answerRepository;

	@Autowired
    public GetAnswersService(QuestionnaireResponseRepository responseRepository, AnswerRepositoryII answerRepository) {
        this.responseRepository = responseRepository;
        this.answerRepository = answerRepository;
    }

	public List<AnswerDTO> getAnswersForResponse(Integer responseId) {

		Optional<QuestionnaireResponseII> responseOptional = responseRepository.findById(responseId);

		if (responseOptional.isPresent()) {
			List<AnswerII> answers = answerRepository.findByQuestionnaireResponseId(responseId);

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
