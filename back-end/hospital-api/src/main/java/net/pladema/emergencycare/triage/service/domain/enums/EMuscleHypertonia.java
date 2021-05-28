package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EMuscleHypertonia {

    HIPERTONIA(1, "Hipertonía"),
    HIPOTONIA(2, "Hipotonía"),
    NORMAL(3, "Normal");

    private final Short id;
    private final String description;

    EMuscleHypertonia(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EMuscleHypertonia> getAll(){
        return Stream.of(EMuscleHypertonia.values()).collect(Collectors.toList());
    }

    @JsonCreator
    public static EMuscleHypertonia getById(Short id){
        for(EMuscleHypertonia emh : values()) {
            if(emh.id.equals(id)) return emh;
        }
        throw new NotFoundException("muscleHypertonia-not-exists", String.format("El valor %s es inválido", id));
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
