package ar.lamansys.sgx.shared.dates.repository.entity;

import ar.lamansys.sgx.shared.dates.exceptions.DateException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum EDayOfWeek {

    SUNDAY(0, "domingo"),
    MONDAY(1, "lunes"),
    TUESDAY(2, "martes"),
    WEDNESDAY(3, "miércoles"),
    THURSDAY(4, "jueves"),
    FRIDAY(5, "viernes"),
    SATURDAY(6, "sábado");

    private Short id;
    private String description;

    EDayOfWeek(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    public static EDayOfWeek map(Short id) {
        for(EDayOfWeek e : values()) {
            if(e.id.equals(id)) return e;
        }
        throw new DateException("day_week-not-exists", String.format("El día de semana %s no existe", id));
    }

    public static List<Short> getAllIds(){
        List<Short> ids = new ArrayList<>();
        for(EDayOfWeek e : values())
            ids.add(e.id);
        return ids;
    }

}
