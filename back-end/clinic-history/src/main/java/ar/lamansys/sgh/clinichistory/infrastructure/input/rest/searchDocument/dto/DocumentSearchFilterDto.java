package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.searchDocument.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.application.searchDocument.domain.EDocumentSearch;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DocumentSearchFilterDto {

    @NotNull
    private String plainText;

    private EDocumentSearch searchType;
}
