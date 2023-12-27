package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceRequestDto implements Serializable {

	private static final long serialVersionUID = -1284231271112634238L;

	private Integer id;

	private List<ClinicalSpecialtyDto> clinicalSpecialties;

	private Integer careLineId;

	private String careLineDescription;

	private String priority;

	private String observation;

	private Short closureTypeId;

	private String closureTypeDescription;

	private DateTimeDto closureDateTime;

	private ProfessionalCompleteDto professionalInfo;

}
