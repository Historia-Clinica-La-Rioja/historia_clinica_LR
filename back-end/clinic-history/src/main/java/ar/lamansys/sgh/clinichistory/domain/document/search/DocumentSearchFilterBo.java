package ar.lamansys.sgh.clinichistory.domain.document.search;

import ar.lamansys.sgh.clinichistory.domain.document.enums.EDocumentSearch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DocumentSearchFilterBo {

    private String plainText;

    private EDocumentSearch searchType;

    public DocumentSearchFilterBo(String plainText, EDocumentSearch searchType) {
        this.plainText = plainText;
        this.searchType = searchType;
    }
}
