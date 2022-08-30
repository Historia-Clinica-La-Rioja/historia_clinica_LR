package ar.lamansys.sgh.publicapi.domain;

import java.util.Arrays;
import java.util.List;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ScopeEnum {
    AMBULATORIA((short) 1, (short) 6),
    INTERNACION((short) 0),
    ENFERMERIA((short) 5);

    private List<Short> ids;

    ScopeEnum(Short... ids){
        this.ids = Arrays.asList(ids);
    }

	public static ScopeEnum map(Short id) {
		for(ScopeEnum e : values()) {
			if(e.ids.contains(id)) return e;
		}
		throw new NotFoundException("scope-not-exists", String.format("El tipo de atención %s no existe", id));
	}
}
