package net.pladema.electronicjointsignature.documentlist.infrastructure.input.rest.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ElectronicJointSignatureInvolvedDocumentListFilterDto {

	@Nullable
	private List<Short> electronicSignaturesStatusIds;

	@Nullable
	private DateDto startDate;

	@Nullable
	private DateDto endDate;

	@Nullable
	private String patientFirstName;

	@Nullable
	private String patientLastName;

}
