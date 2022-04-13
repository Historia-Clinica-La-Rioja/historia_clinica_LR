package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
public class ExternalPatientCoverageBo {

    private ExternalCoverageBo medicalCoverage;

    private String affiliateNumber;

    private Boolean active;

    @Nullable
    private LocalDate vigencyDate;

	private Short condition;


}
