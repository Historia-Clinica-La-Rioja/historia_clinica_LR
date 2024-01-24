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
public enum EAnesthesiaZone {

    REGIONAL((short) 1, "Regional"),
    GENERAL((short) 2, "General"),
    BOTH((short) 3, "Ambas");

    private final Short id;
    private final String description;

    EAnesthesiaZone(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EAnesthesiaZone> getAll() {
        return Stream.of(EAnesthesiaZone.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EAnesthesiaZone map(Short id) {
        for (EAnesthesiaZone e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("anesthesia-zone-not-exists", String.format("El tipo %s no existe", id));
    }
}
