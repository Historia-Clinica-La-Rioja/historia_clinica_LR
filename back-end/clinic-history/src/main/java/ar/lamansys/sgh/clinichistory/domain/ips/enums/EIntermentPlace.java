package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum EIntermentPlace {

    FLOOR((short) 1, "Se interna en piso"),
    INTENSIVE_CARE_UNIT((short) 2, "Se interna en unidad de terapia intensiva");

    private final Short id;
    private final String description;

    EIntermentPlace(Short id, String description) {
        this.id = id;
        this.description = description;
    }
    @JsonCreator
    public static List<EIntermentPlace> getAll() {
        return Stream.of(EIntermentPlace.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EIntermentPlace map(Short id) {
        for (EIntermentPlace e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("internment-place-not-exists", String.format("El tipo %s no existe", id));
    }
}
