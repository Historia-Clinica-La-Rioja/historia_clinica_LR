package ar.lamansys.refcounterref.infraestructure.input.rest.dto.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import ar.lamansys.sgh.shared.infrastructure.input.service.CareLineDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedSnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceDataDto {

    private Integer id;

	private Integer encounterId;

	private Boolean consultation;

	private Integer patientMedicalCoverageId;

    private DateTimeDto date;

	private ReferenceInstitutionDto institutionOrigin;

	private ReferenceInstitutionDto institutionDestination;

	private String note;

    private CareLineDto careLine;

    private ClinicalSpecialtyDto clinicalSpecialtyOrigin;

	private List<ClinicalSpecialtyDto> destinationClinicalSpecialties;

	private SharedSnomedDto procedure;

	private String procedureCategory;

	private String professionalFullName;

    private List<String> problems;

    private List<ReferenceCounterReferenceFileDto> files;

	private EReferencePriority priority;

	private EReferenceClosureType closureType;

	private Integer createdBy;

}
