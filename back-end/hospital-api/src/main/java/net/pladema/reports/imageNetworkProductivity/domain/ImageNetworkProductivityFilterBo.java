package net.pladema.reports.imageNetworkProductivity.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageNetworkProductivityFilterBo {

	private Integer institutionId;

	private LocalDate from;

	private LocalDate to;

	private Short clinicalSpecialtyId;

	private Integer healthcareProfessionalId;

}
