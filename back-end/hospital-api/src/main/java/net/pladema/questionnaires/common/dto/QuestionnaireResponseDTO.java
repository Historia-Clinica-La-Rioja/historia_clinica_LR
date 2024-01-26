package net.pladema.questionnaires.common.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuestionnaireResponseDTO {

	private Integer questionnaireId;

	private List<AnswerDTO> answers;

}
