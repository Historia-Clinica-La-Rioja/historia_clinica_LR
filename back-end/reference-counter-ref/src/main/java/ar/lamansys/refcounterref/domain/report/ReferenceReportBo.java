package ar.lamansys.refcounterref.domain.report;

import ar.lamansys.refcounterref.domain.enums.EReferenceAttentionState;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import ar.lamansys.refcounterref.domain.snomed.SnomedBo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class ReferenceReportBo {

	private Integer id;

	private String patientFirstName;

	private String patientMiddleNames;

	private String patientLastName;

	private String patientOtherLastNames;

	private String patientNameSelfDetermination;

	private String patientFullName;

	private String identificationType;

	private String identificationNumber;

	private List<String> problems;
	
	private EReferencePriority priority;

	private LocalDateTime date;

	private String clinicalSpecialtyOrigin;

	private String institutionOrigin;

	private String institutionDestination;

	private List<String> destinationClinicalSpecialties;

	private String careLine;

	private EReferenceClosureType closureType;

	private EReferenceAttentionState attentionState;

	private SnomedBo procedure;

	private EReferenceRegulationState regulationState;

	private String forwardingType;
}
