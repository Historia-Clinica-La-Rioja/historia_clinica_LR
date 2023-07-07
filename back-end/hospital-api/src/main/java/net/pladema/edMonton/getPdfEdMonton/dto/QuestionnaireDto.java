package net.pladema.edMonton.getPdfEdMonton.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.edMonton.get.controller.dto.EdMontonAnswers;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireDto {

	private Integer idQuestionnaire;

	List<EdMontonAnswers> answers;

	public QuestionnaireDto(List<EdMontonAnswers> lstEdMonton) {
		this.answers = lstEdMonton;
	}
}
