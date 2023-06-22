package net.pladema.edMonton.getPdfEdMonton.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.edMonton.repository.domain.Answer;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImpresionEdMontonDto {

	private Integer questionId;

	private Integer answerId;

	private Integer value;


	public ImpresionEdMontonDto(Answer answer){
		this.questionId = answer.getItemId();
		this.answerId = answer.getAnswerId();
		//this.value = Integer.valueOf(answer.getValue());
	}
}
