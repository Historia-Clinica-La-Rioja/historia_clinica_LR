package net.pladema.template.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EDocumentTemplate {

    REPORT_DETAILS_RDI(1, "Detalle informe RDI");

    private final Short id;
    private final String description;

    EDocumentTemplate(Integer id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    public static EDocumentTemplate map(Short id) {
        for (EDocumentTemplate e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("document-template-not-exists", String.format("La plantilla %s no existe", id));
    }
}
