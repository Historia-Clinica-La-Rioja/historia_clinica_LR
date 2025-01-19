package ar.lamansys.sgh.shared.infrastructure.input.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    public Integer getId() {
        if (medicalCoverage != null) {
            return medicalCoverage.getId();
        }
        return null;
    }
}
