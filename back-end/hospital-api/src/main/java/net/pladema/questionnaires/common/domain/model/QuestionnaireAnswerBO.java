package net.pladema.questionnaires.common.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireAnswerBO {

	private Short questionId;

	private Short answerId;

	private Integer value;

	public QuestionnaireAnswerBO(Short questionId, Short answerId) {
		this.questionId = questionId;
		this.answerId = answerId;
	}

	public QuestionnaireAnswerBO(Short questionId, Integer value) {
		this.questionId = questionId;
		this.value = value;
	}
}
