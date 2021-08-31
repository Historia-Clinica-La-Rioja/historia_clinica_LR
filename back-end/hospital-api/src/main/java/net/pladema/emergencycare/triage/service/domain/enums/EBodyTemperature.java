package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EBodyTemperature {

    FIEBRE(1, "Fiebre"),
    HIPOTERMIA(2, "Hipotermia"),
    NORMAL(3, "Normal");

    private final Short id;
    private final String description;

    EBodyTemperature(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EBodyTemperature> getAll(){
        return Stream.of(EBodyTemperature.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EBodyTemperature getById(Short id){
        for(EBodyTemperature ebt : values()) {
            if(ebt.id.equals(id)) return ebt;
        }
        throw new NotFoundException("bodyTemperature-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
