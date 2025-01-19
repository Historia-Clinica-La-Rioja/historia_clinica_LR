package net.pladema.hl7.dataexchange.model.domain;

import lombok.Getter;
import lombok.Setter;

import org.hl7.fhir.r4.model.Quantity;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class MedicationDispenseVo {

	private String identifier;
	private String status;
	private Short statusId;
	private String category;

	private String medicationId; // Medicamento comercial dispensado. (Obligatorio)
	private UUID lineUuid;
	private UUID requestUuid;
	private String prescriptionLineNumber;
	private String patientId; // Referencia al Paciente
	private String performerId; // Aquel individuo que realiza la entega de medicamentos
	private String medicationRequestId; // Referencia a la prescripción que autoriza la dispensación.
	private String locationId;
	private Date whenHandedOver;
	private String note; // Información sobre la receta
	private DosageVo dosage; // (igual que el MR)
	private Quantity quantity;
	private String medicationSnomedCode;

	private PatientVo patientVo;
	private CoverageVo coverageVo;
	private PractitionerVo practitionerVo;
	private OrganizationVo organizationVo;
	private LocationVo locationVo;
	private MedicationRequestVo medicationRequestVo;

	public boolean hasNote() {
		return (note != null && !note.isBlank() && !note.isEmpty());
	}
}
