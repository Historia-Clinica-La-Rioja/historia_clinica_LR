package net.pladema.edMonton.create.controller.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EdMontonBo {

	private Integer patientId;

	private Integer result;

	private List<EdMontonAnswerBo> answers;
}
