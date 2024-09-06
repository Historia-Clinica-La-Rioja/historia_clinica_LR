package net.pladema.reports.imageNetworkProductivity.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImageNetworkProductivityFilterBo {

	private Integer institutionId;

	private LocalDate from;

	private LocalDate to;

	private Short clinicalSpecialtyId;

	private Integer healthcareProfessionalId;

}
