package ar.lamansys.sgh.clinichistory.application.searchDocument.domain;

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
