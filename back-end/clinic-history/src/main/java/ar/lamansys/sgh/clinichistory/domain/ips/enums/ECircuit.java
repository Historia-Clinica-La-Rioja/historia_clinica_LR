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
public enum ECircuit {
    OPEN((short) 1, "Abierto"),
    CLOSE((short) 2, "Cerrado"),
    CIRCULAR((short) 3, "Circular"),
    ;

    private final Short id;
    private final String description;

    ECircuit(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<ECircuit> getAll() {
        return Stream.of(ECircuit.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static ECircuit map(Short id) {
        for (ECircuit e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("circuit-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
