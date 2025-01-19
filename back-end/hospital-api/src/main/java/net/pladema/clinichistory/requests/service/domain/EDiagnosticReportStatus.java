package net.pladema.clinichistory.requests.service.domain;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import net.pladema.clinichistory.requests.servicerequests.domain.enums.EStudyType;

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

    public Boolean isFinal() {
    	return this.equals(FINAL);
    }

	public static EDiagnosticReportStatus map(String id) {
		for (EDiagnosticReportStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("status-type-not-exists", String.format("El tipo de estado %s no es válido", id));
	}
}
