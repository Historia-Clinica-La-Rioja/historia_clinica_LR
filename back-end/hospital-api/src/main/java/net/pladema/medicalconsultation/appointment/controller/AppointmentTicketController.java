package net.pladema.medicalconsultation.appointment.controller;


import java.util.Map;

import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketImageDto;

import net.pladema.medicalconsultation.appointment.service.impl.TicketServiceImpl;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointment-ticket-report")
@Slf4j
@AllArgsConstructor
public class AppointmentTicketController {

	private final AppointmentService appointmentService;

	private final PdfService pdfService;

	private final TicketServiceImpl ticketServiceImpl;

	@GetMapping("/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA, ADMINISTRATIVO_RED_DE_IMAGENES, GESTOR_DE_ACCESO_INSTITUCIONAL') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<Resource> getTicket(@PathVariable(name = "institutionId") Integer institutionId,
											  @PathVariable(name = "appointmentId") Integer appointmentId) throws PDFDocumentException {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		var bo = appointmentService.getAppointmentTicketData(appointmentId);
		var dto = new AppointmentTicketDto(bo);
		Map<String, Object> context = ticketServiceImpl.createContext(dto);
		String outputFileName = ticketServiceImpl.createOutputFileName(dto);
		log.debug("outputFileName " + outputFileName);

		return StoredFileResponse.sendFile(
				pdfService.generate("appointment-ticket", context),
				outputFileName,
				MediaType.APPLICATION_PDF
		);
	}

	@GetMapping("{appointmentId}/image/transcribed-order/{transcribed}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA, ADMINISTRATIVO_RED_DE_IMAGENES') || hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public ResponseEntity<Resource> getTicketImage(@PathVariable(name = "institutionId") Integer institutionId,
												   @PathVariable(name = "appointmentId") Integer appointmentId,
												   @PathVariable(name = "transcribed") Boolean isTranscribed
	) throws PDFDocumentException {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		var bo = appointmentService.getAppointmentImageTicketData(appointmentId, isTranscribed);
		var dto = new AppointmentTicketImageDto(bo);
		Map<String, Object> context = ticketServiceImpl.createContextImage(dto);
		String outputFileName = ticketServiceImpl.createOutputFileNameImage(dto);
		log.debug("outputFileName " + outputFileName);

		return StoredFileResponse.sendFile(
				pdfService.generate("appointment-ticket-image", context),
				outputFileName,
				MediaType.APPLICATION_PDF
		);
	}

}
