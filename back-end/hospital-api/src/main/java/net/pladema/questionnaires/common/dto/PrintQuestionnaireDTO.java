package net.pladema.questionnaires.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.questionnaires.common.domain.Answer;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrintQuestionnaireDTO {

	private Integer questionId;

	private Integer answerId;

	private Integer value;

	public PrintQuestionnaireDTO(Answer answer){
		this.questionId = answer.getItemId();
		this.answerId = answer.getAnswerId();
		this.value = Integer.valueOf(answer.getValue());
	}
}
