package ar.lamansys.sgh.clinichistory.domain.ips.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum EPersonalHistoryType {
    HABIT((short) 1, "Hábito"),
    CLINICAL((short) 2, "Clínico"),
    SURGICAL((short) 3, "Quirúrgico");

    private final Short id;
    private final String description;

    EPersonalHistoryType(Short id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EPersonalHistoryType> getAll() {
        return Stream.of(EPersonalHistoryType.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EPersonalHistoryType map(Short id) {
        for (EPersonalHistoryType e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("personal-history-type-not-exists", String.format("El tipo %s no existe", id));
    }
}
