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
public enum ETrachealIntubation {

    OROTRACHEAL((short) 1, "Orotraqueal"),
    NASOTRACHEAL((short) 2, "Nasotraqueal"),
    ;

    private final Short id;
    private final String description;

    ETrachealIntubation(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<ETrachealIntubation> getAll() {
        return Stream.of(ETrachealIntubation.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static ETrachealIntubation map(Short id) {
        for (ETrachealIntubation e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("tracheal-intubation-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
