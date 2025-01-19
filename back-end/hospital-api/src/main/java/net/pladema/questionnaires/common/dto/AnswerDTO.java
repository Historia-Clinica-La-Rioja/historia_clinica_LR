package net.pladema.questionnaires.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnswerDTO {

	private Integer itemId;

	private Integer optionId;

	private String value;
}
