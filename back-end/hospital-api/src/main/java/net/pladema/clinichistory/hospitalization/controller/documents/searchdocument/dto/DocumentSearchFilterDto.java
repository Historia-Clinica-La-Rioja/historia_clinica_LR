package net.pladema.clinichistory.hospitalization.controller.documents.searchdocument.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.searchdocument.enums.EDocumentSearch;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DocumentSearchFilterDto {

    @NotNull
    private String plainText;

    private EDocumentSearch searchType = EDocumentSearch.ALL;
}
