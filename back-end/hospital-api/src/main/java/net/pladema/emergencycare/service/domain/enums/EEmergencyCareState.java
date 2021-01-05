package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareState {

    ATENCION(1, "En atención"),
    ESPERA(2, "En espera"),
    AUSENCIA(3, "Finalizado por ausencia"),
    ALTA(4, "Finalizado por alta");

    private final Short id;
    private final String description;

    EEmergencyCareState(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static Collection<EEmergencyCareState> getAll(){
        return Stream.of(EEmergencyCareState.values()).collect(Collectors.toList());
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
