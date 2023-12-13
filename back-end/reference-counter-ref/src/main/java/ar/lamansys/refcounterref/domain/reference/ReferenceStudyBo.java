package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReferenceStudyBo {

	private SnomedBo problem;

	private SnomedBo practice;

	private String categoryId;

}
