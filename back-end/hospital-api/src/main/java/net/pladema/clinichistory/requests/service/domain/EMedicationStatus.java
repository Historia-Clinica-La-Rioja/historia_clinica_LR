package net.pladema.clinichistory.requests.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EMedicationStatus {

    ACTIVE("55561003", "Activa"),
    SUSPENDED("385655000", "Suspendida"),
    STOPPED("6155003", "Finalizada");

    private final String id;
    private final String description;

    EMedicationStatus(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EMedicationStatus> getAll(){
        return Stream.of(EMedicationStatus.values()).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
