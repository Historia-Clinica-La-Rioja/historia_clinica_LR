package ar.lamansys.refcounterref.domain.report;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class ReferenceReportFilterBo {

	@Nullable
	private String identificationNumber;

	@Nullable
	private Integer closureTypeId;

	@Nullable
	private Short attentionStateId;

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

	@Nullable
	private Integer managerUserId;

	@Nullable
	private List<Short> loggedUserRoleIds;

	@Nullable
	private Short regulationStateId;

	@Nullable
	private Integer careLineId;

	@Nullable
	private Integer institutionalGroupId;

	@Nullable
	private Integer destinationDepartmentId;

	@Nullable
	private boolean domainManager;

	@Nullable
	private Short administrativeStateId;

	@Nullable
	private boolean isReceived = false;

}
