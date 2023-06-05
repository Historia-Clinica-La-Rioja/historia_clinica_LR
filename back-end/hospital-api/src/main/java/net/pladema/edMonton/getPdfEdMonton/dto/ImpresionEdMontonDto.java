package net.pladema.edMonton.getPdfEdMonton.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImpresionEdMontonDto {

	private Short questionId;

	private Short answerId;


	public ImpresionEdMontonDto(ImpresionEdMontonDto impresionEdMontonDto) {
		this.questionId = impresionEdMontonDto.getQuestionId();
		this.answerId = impresionEdMontonDto.getAnswerId();
	}
}
