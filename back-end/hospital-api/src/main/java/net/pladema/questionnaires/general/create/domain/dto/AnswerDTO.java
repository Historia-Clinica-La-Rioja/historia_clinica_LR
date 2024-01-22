package net.pladema.questionnaires.general.create.domain.dto;

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
