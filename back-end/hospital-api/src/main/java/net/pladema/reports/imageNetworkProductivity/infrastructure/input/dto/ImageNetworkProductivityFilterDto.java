package net.pladema.reports.imageNetworkProductivity.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageNetworkProductivityFilterDto {

	private DateDto from;

	private DateDto to;

	private Short clinicalSpecialtyId;

	private Integer healthcareProfessionalId;

}
