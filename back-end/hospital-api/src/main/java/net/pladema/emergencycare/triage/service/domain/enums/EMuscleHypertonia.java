package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

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

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
