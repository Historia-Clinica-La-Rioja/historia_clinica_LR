package net.pladema.edMonton.create.controller.service.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EdMontonBo {

	private Integer patientId;

	private Integer result;

	private List<EdMontonAnswerBo> answers;
}
