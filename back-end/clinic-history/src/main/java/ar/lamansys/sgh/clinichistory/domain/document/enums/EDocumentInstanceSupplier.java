package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgh.clinichistory.domain.document.DocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.IDocumentBo;
import ar.lamansys.sgh.clinichistory.domain.document.impl.AnestheticReportBo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
@Getter
public enum EDocumentInstanceSupplier {

    DEFAULT_DOCUMENT((short) 0, DocumentBo::new),
    ANESTHETIC_REPORT((short) 20, AnestheticReportBo::new)
    ;

    private final Short id;
    private final Supplier<IDocumentBo> documentBoSupplier;

    public static EDocumentInstanceSupplier map(Short id) {
        for (EDocumentInstanceSupplier e : values()) {
            if (e.id.equals(id)) return e;
        }
        return DEFAULT_DOCUMENT;
    }

}
