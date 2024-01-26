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

	public ReferenceStudyBo(String problemSctid, String problemPt, String practiceSctid, String practicePt, String categoryId){
		this.problem = new SnomedBo(problemSctid, problemPt);
		this.practice = new SnomedBo(practiceSctid, practicePt);
		this.categoryId = categoryId;
	}

}
