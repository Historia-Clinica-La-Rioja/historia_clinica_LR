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
public enum EBreathing {
    SPONTANEOUS((short) 1, "Espontánea"),
    MANUAL((short) 2, "Manual"),
    ASSISTED((short) 3, "Asistida"),
    ;

    private final Short id;
    private final String description;

    EBreathing(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EBreathing> getAll() {
        return Stream.of(EBreathing.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EBreathing map(Short id) {
        for (EBreathing e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("breathing-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
