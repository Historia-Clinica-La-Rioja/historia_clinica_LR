package ar.lamansys.sgh.clinichistory.application.anestheticreport.chart.image.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EImageFileExtension {
    PNG((short) 1, ".png"),
    SVG((short) 2, ".svg"),
    ;

    private final Short id;
    private final String description;

    EImageFileExtension(short i, String description) {
        this.id = i;
        this.description = description;
    }

    public static EImageFileExtension map(Short id) {
        for(EImageFileExtension e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("image-extension-not-exists", String.format("La extensión %s no existe", id));
    }
}
