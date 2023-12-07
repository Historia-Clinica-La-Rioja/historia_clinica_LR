package net.pladema.clinichistory.requests.servicerequests.infrastructure.input.service;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EDiagnosticImageReportStatus {
	
	COMPLETED(1, "Completado"),
	DERIVED(2, "Derivado"),
	PENDING(3, "Pendiente"),
	NOT_REQUIRED(4, "No requerido");

	private final Short id;
	private final String description;

	EDiagnosticImageReportStatus(Number id, String description) {
		this.id = id.shortValue();
		this.description = description;
	}

	public Short getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static List<EDiagnosticImageReportStatus> getAll(){
		return Stream.of(EDiagnosticImageReportStatus.values()).collect(Collectors.toList());
	}
}
