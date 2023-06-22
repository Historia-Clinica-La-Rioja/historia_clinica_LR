package net.pladema.edMonton.create.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdMontonAnswerDto {

	private Short questionId;

	private Short answerId;

	private String value;
}
