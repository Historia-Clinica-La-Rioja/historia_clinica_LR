package net.pladema.questionnaires.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionnaireAnswerDTO {

	private Short questionId;

	private Short answerId;

	private String value;

}
