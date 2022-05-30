package ar.lamansys.online.infraestructure.input.rest.specialty.dto;

import ar.lamansys.online.domain.specialty.PracticeBo;
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

    public PracticeDto(PracticeBo practiceBo){
        this.id = practiceBo.getId();
        this.description = practiceBo.getDescription();
        this.coverage = practiceBo.getCoverage();
        this.coverageText = practiceBo.getCoverageText();
        this.snomedId = practiceBo.getSnomedId();
    }
}
