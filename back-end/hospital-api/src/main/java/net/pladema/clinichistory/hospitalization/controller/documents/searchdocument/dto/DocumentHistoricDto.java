package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class DocumentHistoricDto {

    private List<DocumentSearchDto> documents;

    private String message;
}
