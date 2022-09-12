package net.pladema.medicalconsultation.appointment.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.pdf.PdfService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.medicalconsultation.appointment.controller.dto.AppointmentTicketDto;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;

@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/appointment-ticket-report")
@Slf4j
@AllArgsConstructor
public class AppointmentTicketController {

	public static final String OUTPUT = "Output -> {}";

	private final AppointmentService appointmentService;

	private final PdfService pdfService;

	@GetMapping("/{appointmentId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<InputStreamResource> getTicket(@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "appointmentId") Integer appointmentId) throws PDFDocumentException {
		log.debug("Input parameters -> appointmentId {}", appointmentId);
		var bo = appointmentService.getAppointmentTicketData(appointmentId);
		var dto = new AppointmentTicketDto(bo);
		Map<String, Object> context = createContext(dto);
		String outputFileName = createOutputFileName(dto);
		log.debug("outputFileName " + outputFileName);
		ResponseEntity<InputStreamResource> response = generatePdfResponse(context, outputFileName);
		log.debug(OUTPUT, response);
		return response;
	}

	private ResponseEntity<InputStreamResource> generatePdfResponse(Map<String, Object> context, String outputFileName) throws PDFDocumentException {
		log.debug("Input parameters -> context {}, outputFileName {}", context, outputFileName);
		ByteArrayOutputStream outputStream = pdfService.writer("appointment-ticket", context);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
		InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
		ResponseEntity<InputStreamResource> response;
		response = ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + outputFileName)
				.contentType(MediaType.APPLICATION_PDF).contentLength(outputStream.size()).body(resource);
		return response;
	}

	private Map<String, Object> createContext(AppointmentTicketDto dto){
		log.debug("Input parameters -> AppointmentTicketDto {}", dto);
		Map<String, Object> ctx = new HashMap<>();
		ctx.put("institution", dto.getInstitution());
		ctx.put("dni", dto.getDocumentNumber());
		ctx.put("patientFullName", dto.getPatientFullName());
		ctx.put("medicalCoverage", dto.getMedicalCoverage());
		ctx.put("date", dto.getDate());
		ctx.put("hour", dto.getHour());
		ctx.put("doctorsOffice", dto.getDoctorsOffice());
		ctx.put("doctorFullName", dto.getDoctorFullName());
		log.debug(OUTPUT, ctx);
		return ctx;
	}

	private String createOutputFileName(AppointmentTicketDto dto){
		log.debug("Input parameters -> AppointmentTicketDto {}", dto);
		String outputFileName = "Turno_" + dto.getPatientFullName() + "_" + dto.getDate() + "_" + dto.getHour();
		log.debug(OUTPUT, outputFileName);
		return outputFileName;
	}

}
