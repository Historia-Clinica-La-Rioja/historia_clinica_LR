package ar.lamansys.refcounterref.domain.report;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ReferenceReportFilterBo {

	@Nullable
	private String identificationNumber;

	@Nullable
	private Integer closureTypeId;

	@Nullable
	private Short appointmentStateId;

	@Nullable
	private Integer clinicalSpecialtyId;

	@Nullable
	private Integer procedureId;

	@Nullable
	private Short priorityId;

	@NotNull
	private LocalDate from;

	@NotNull
	private LocalDate to;

	@Nullable
	private Integer originInstitutionId;

	@Nullable
	private Integer healthcareProfessionalId;

	@Nullable
	private Integer destinationInstitutionId;
}
