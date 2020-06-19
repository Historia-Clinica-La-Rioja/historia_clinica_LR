package net.pladema.clinichistory.documents.service.searchdocument.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DocumentHistoricBo {

    private List<DocumentSearchBo> documents;

    private String message;
}
