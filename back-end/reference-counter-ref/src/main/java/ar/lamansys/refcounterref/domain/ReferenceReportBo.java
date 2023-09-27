package ar.lamansys.refcounterref.domain;

import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@Builder
@ToString
public class ReferenceReportBo {

	private Integer referenceId;

	private String patientFullName;

	private String identificationType;

	private String identificationNumber;

	private List<String> problems;
	
	private EReferencePriority priority;

	private LocalDateTime date;

	private String clinicalSpecialtyOrigin;

	private String institutionOrigin;

	private String institutionDestination;

	private String clinicalSpecialtyDestination;

	private String careLine;

	private EReferenceClosureType closureType;

	private Short appointmentStateId;

}
