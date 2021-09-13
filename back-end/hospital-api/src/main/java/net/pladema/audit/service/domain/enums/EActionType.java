package net.pladema.audit.service.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EActionType {

    CREATE(1, "Alta"),
    UPDATE(2, "Edición");

    private final Short id;
    private final String description;

    EActionType(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EActionType> getAll() {
        return Arrays.asList(EActionType.values());
    }

    @JsonCreator
    public static EActionType getById(Short id) {
        if (id == null)
            return null;
        return Stream.of(values())
                .filter(eat -> id.equals(eat.getId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("actionType-not-exists", String.format("El valor %s es inválido", id)));

    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}