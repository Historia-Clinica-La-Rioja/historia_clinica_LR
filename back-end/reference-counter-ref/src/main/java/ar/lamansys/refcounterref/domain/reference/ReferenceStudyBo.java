package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ReferenceStudyBo {

	private SnomedBo problem;

	private SnomedBo practice;

	private String categoryId;

	public ReferenceStudyBo(Integer problemId, String problemSctid, String problemPt, Integer practiceId, String practiceSctid, String practicePt, String categoryId){
		this.problem = new SnomedBo(problemId, problemSctid, problemPt);
		this.practice = new SnomedBo(practiceId, practiceSctid, practicePt);
		this.categoryId = categoryId;
	}

}
