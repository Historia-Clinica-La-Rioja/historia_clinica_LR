package net.pladema.edMonton.create.controller.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EdMontonAnswerBo {

	private Short questionId;

	private Short answerId;

	private Integer value;

	public EdMontonAnswerBo(Short questionId, Short answerId){
		this.questionId = questionId;
		this.answerId = answerId;
	}
	public EdMontonAnswerBo(Short questionId, Integer value){
		this.questionId= questionId;
		this.value = value;
	}
}
