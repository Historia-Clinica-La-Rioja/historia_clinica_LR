package net.pladema.clinichistory.hospitalization.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DocumentHistoricDto {

    private List<DocumentSearchDto> documents;
}
