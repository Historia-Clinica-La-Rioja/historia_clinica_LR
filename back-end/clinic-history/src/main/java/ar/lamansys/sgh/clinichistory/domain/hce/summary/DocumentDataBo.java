package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;

@Getter
public class DocumentDataBo {

    private Long id;

    private String filename;

    public DocumentDataBo(Long id, String filename) {
        this.id = id;
        this.filename = filename;
    }
}
