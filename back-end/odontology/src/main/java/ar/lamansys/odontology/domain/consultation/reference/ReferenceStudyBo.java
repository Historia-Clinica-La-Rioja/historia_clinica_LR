package ar.lamansys.odontology.domain.consultation.reference;

import ar.lamansys.odontology.domain.OdontologySnomedBo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferenceStudyBo {

	private OdontologySnomedBo problem;

	private OdontologySnomedBo practice;

	private String categoryId;

}
