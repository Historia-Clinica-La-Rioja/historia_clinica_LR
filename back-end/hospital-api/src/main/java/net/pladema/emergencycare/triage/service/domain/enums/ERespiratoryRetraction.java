package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ERespiratoryRetraction {

    INTERCOSTAL(1, "Intercostal"),
    GENERALIZADO(2, "Generalizado"),
    NO((short)3, "No");

    private final Short id;
    private final String description;

    ERespiratoryRetraction(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<ERespiratoryRetraction> getAll(){
        return Stream.of(ERespiratoryRetraction.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static ERespiratoryRetraction getById(Short id){
        for(ERespiratoryRetraction err : values()) {
            if(err.id.equals(id)) return err;
        }
        throw new NotFoundException("respiratoryRetraction-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
