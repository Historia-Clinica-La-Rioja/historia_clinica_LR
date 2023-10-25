package net.pladema.questionnaires.common.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionnaireBO {

	private Integer patientId;

	private List<QuestionnaireAnswerBO> answers;

}
