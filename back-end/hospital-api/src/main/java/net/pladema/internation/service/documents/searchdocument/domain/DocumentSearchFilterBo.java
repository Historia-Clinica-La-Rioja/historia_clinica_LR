package net.pladema.internation.service.documents.searchdocument.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.internation.service.documents.searchdocument.enums.EDocumentSearch;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DocumentSearchFilterBo {

    private String plainText;

    private EDocumentSearch searchType = EDocumentSearch.DIAGNOSIS;
}
