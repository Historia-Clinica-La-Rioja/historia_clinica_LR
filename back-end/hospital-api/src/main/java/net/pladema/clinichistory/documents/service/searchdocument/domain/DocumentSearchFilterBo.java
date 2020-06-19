package net.pladema.clinichistory.documents.service.searchdocument.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.service.searchdocument.enums.EDocumentSearch;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DocumentSearchFilterBo {

    private String plainText;

    private EDocumentSearch searchType = EDocumentSearch.DIAGNOSIS;
}
