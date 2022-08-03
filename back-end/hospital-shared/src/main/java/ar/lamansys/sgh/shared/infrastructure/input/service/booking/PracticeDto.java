package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PracticeDto {
    private final Integer id;
    private final String description;
    private final Boolean coverage;
    private final String coverageText;
    private final Integer snomedId;

	public PracticeDto(Integer id, String description, Boolean coverage, String coverageText, Integer snomedId) {
		this.id = id;
		this.description = description;
		this.coverage = coverage;
		this.coverageText = coverageText;
		this.snomedId = snomedId;
	}
}
