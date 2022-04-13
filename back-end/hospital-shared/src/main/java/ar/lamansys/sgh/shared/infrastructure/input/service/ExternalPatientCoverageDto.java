package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ExternalPatientCoverageDto {

    private ExternalCoverageDto medicalCoverage;

    private String affiliateNumber;

    private Boolean active;

    @Nullable
    private LocalDate vigencyDate;

	@Nullable
	private Short condition;
}
