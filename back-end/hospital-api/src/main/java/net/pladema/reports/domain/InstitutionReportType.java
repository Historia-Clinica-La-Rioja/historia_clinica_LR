package net.pladema.reports.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InstitutionReportType {
	Monthly("Detalle Nominal de Consultorios Externos - Hoja 2"),
	AppointmentNominalDetail("Detalle Nominal de Turnos"),
	EmergencyCareNominalDetail("Reporte detalle Nominal atenciones de guardia"),
	ImageNetworkProductivity("Detalle nominal de prestaciones RDI"),
	;

	private final String description;
}
