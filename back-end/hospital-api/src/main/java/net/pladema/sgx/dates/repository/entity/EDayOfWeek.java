package net.pladema.sgx.dates.repository.entity;

import lombok.Getter;
import net.pladema.sgx.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum EDayOfWeek {

    DOMINGO(0, "domingo"),
    LUNES(1, "lunes"),
    MARTES(2, "martes"),
    MIERCOLES(3, "miércoles"),
    JUEVES(4, "jueves"),
    VIERNES(5, "viernes"),
    SABADO(6, "sábado");

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
        throw new NotFoundException("day_week-not-exists", String.format("El día de semana %s no existe", id));
    }

    public static List<Short> getAllIds(){
        List<Short> ids = new ArrayList<>();
        for(EDayOfWeek e : values())
            ids.add(e.id);
        return ids;
    }

}
