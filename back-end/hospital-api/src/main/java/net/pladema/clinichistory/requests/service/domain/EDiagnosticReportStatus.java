package net.pladema.clinichistory.requests.service.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EDiagnosticReportStatus {

    REGISTERED("1", "Pendiente"),
    FINAL("261782000", "Completado");
    
    private final String id;
    private final String description;

    EDiagnosticReportStatus(String id, String description) {
        this.id = id;
        this.description = description;
    }

    @JsonCreator
    public static List<EDiagnosticReportStatus> getAll(){
        return Stream.of(EDiagnosticReportStatus.values()).collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
