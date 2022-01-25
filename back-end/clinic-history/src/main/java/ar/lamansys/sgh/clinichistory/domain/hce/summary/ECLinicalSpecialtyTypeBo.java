package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ECLinicalSpecialtyTypeBo {

    SERVICE((short) 1),
    SPECIALTY((short) 2),
    ;

    private Short id;

    ECLinicalSpecialtyTypeBo(Short id) {
        this.id = id;
    }
 

	public static ECLinicalSpecialtyTypeBo map(Short id) {
        for(ECLinicalSpecialtyTypeBo e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new NotFoundException("clinical-specialty-type-not-exist", String.format("El tipo de especialidad clinica %s no existe", id));
    }
}
