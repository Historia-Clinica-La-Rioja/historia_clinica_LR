package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DocumentTypeBo {

	private Short id;
	private String description;
}