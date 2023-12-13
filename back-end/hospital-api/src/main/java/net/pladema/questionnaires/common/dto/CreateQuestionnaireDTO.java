package net.pladema.questionnaires.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionnaireDTO {

	private List<QuestionnaireAnswerDTO> questionnaire;

}
