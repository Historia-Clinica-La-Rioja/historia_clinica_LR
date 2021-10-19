package ar.lamansys.sgh.publicapi.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ScopeEnum {
    AMBULATORIA((short) 1, (short) 6),
    INTERNACION((short) 0),
    ENFERMERIA((short) 5);

    private List<Short> ids;

    ScopeEnum(Short... ids){
        this.ids = Arrays.asList(ids);
    }
}
