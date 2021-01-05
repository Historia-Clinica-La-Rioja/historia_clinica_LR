package net.pladema.emergencycare.triage.service.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EPerfusion {

    NORMAL(1, "Normal"),
    ALTERADA(2, "Alterada");

    private final Short id;
    private final String description;

    EPerfusion(Number id, String description) {
        this.id = id.shortValue();
        this.description = description;
    }

    @JsonCreator
    public static List<EPerfusion> getAll(){
        return Stream.of(EPerfusion.values()).collect(Collectors.toList());
    }

    public Short getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
