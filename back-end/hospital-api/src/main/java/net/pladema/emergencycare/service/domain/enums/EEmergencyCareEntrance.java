package net.pladema.emergencycare.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EEmergencyCareEntrance {

    CAMINANDO(1, "Caminando"),
    SILLA(2, "En silla de ruedas"),
    CONMEDICO(3, "Ambulancia con médico"),
    SINMEDICO(4, "Ambulancia sin médico");

    private final Short id;
    private final String description;

    EEmergencyCareEntrance(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static Collection<EEmergencyCareEntrance> getAll(){
        return Stream.of(EEmergencyCareEntrance.values()).collect(Collectors.toList());
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}