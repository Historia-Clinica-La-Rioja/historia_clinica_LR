package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceRequestDto implements Serializable {

	private static final long serialVersionUID = -1284231271112634238L;

	private Integer id;
	private Integer clinicalSpecialtyId;
	private String clinicalSpecialtyName;
	private Integer careLineId;
	private String careLineDescription;
	private String priority;
	private String observation;

}
