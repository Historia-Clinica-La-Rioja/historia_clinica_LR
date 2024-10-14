package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class ReferenceSummaryBo {

	private Integer id;

	private Integer careLineId;

	private String institution;

	private LocalDate date;

	private String firstName;

	private String middleNames;

	private String lastName;

	private String otherLastNames;

	private String nameSelfDetermination;

	private boolean includeNameSelfDetermination;

	private Integer destinationInstitutionId;

	private EReferenceRegulationState regulationState;


	public ReferenceSummaryBo(Integer id, String institutionName,
							  LocalDate date, String firstName, String middleNames, String lastName,
							  String otherLastNames, String nameSelfDetermination, Integer careLineId, Integer destinationInstitutionId) {
		this.id = id;
		this.institution = institutionName;
		this.date = date;
		this.firstName = firstName;
		this.middleNames = middleNames;
		this.lastName = lastName;
		this.otherLastNames = otherLastNames;
		this.nameSelfDetermination = nameSelfDetermination;
		this.careLineId = careLineId;
		this.destinationInstitutionId = destinationInstitutionId;
	}


	public String getProfessionalFullName() {
		String name = null;
		if (includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank())) {
			name = nameSelfDetermination;
			middleNames = null;
		} else name = firstName;
		return Stream.of(lastName, otherLastNames, name, middleNames)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(" "));
	}
}
