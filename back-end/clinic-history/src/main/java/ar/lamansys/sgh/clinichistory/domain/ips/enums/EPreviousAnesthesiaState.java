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
public enum EPreviousAnesthesiaState {

    YES((short) 1, "Si"),
    NO((short) 2, "No"),
    CANT_ANSWER((short) 3, "No puede contestar");

    private final Short id;
    private final String description;

    EPreviousAnesthesiaState(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EPreviousAnesthesiaState> getAll() {
        return Stream.of(EPreviousAnesthesiaState.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EPreviousAnesthesiaState map(Short id) {
        for (EPreviousAnesthesiaState e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("previous-anesthesia-state-not-exists", String.format("El tipo %s no existe", id));
    }
}
