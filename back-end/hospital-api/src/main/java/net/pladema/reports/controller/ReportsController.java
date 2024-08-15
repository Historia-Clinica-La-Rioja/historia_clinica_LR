package net.pladema.reports.controller;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.application.annex.SharedAppointmentAnnexPdfReportService;

import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.hsi.extensions.infrastructure.controller.dto.UIComponentDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;

import net.pladema.reports.application.ReportInstitutionQueryBo;
import net.pladema.reports.application.fetchappointmentconsultationsummary.FetchAppointmentConsultationSummary;

import net.pladema.reports.application.generators.GenerateEmergencyCareNominalDetailExcelReport;
import net.pladema.reports.application.generators.GenerateInstitutionMonthlyExcelReport;
import net.pladema.reports.application.generators.GenerateAppointmentNominalDetailExcelReport;
import net.pladema.reports.domain.AnnexIIParametersBo;
import net.pladema.reports.domain.FormVParametersBo;
import net.pladema.reports.domain.ReportSearchFilterBo;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.dates.configuration.LocalDateMapper;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.StreamsUtils;
import ar.lamansys.sgx.shared.files.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import ar.lamansys.sgx.shared.filestorage.application.FileContentBo;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import ar.lamansys.sgx.shared.reports.util.struct.IWorkbook;
import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.controller.dto.ConsultationsDto;
import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.controller.mapper.ReportsMapper;
import net.pladema.reports.service.AnnexReportService;
import net.pladema.reports.service.FetchConsultations;
import net.pladema.reports.service.FormReportService;
import net.pladema.reports.service.domain.AnnexIIBo;
import net.pladema.reports.service.domain.ConsultationSummaryReport;
import net.pladema.reports.service.domain.ConsultationsBo;
import net.pladema.reports.service.domain.FormVBo;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/reports")
@RestController
public class ReportsController {

    public static final String OUTPUT = "Output -> {}";

    private final ConsultationSummaryReport consultationSummaryReport;
    private final LocalDateMapper localDateMapper;
    private final PdfService pdfService;
    private final AnnexReportService annexReportService;
    private final FormReportService formReportService;
    private final ReportsMapper reportsMapper;
    private final FetchConsultations fetchConsultations;
	private final FeatureFlagsService featureFlagsService;
	private final GenerateInstitutionMonthlyExcelReport generateInstitutionMonthlyExcelReport;
	private final GenerateAppointmentNominalDetailExcelReport generateAppointmentNominalDetailExcelReport;
	private final SharedAppointmentAnnexPdfReportService sharedAppointmentAnnexPdfReportService;
	private final GenerateEmergencyCareNominalDetailExcelReport generateEmergencyCareNominalDetailExcelReport;
	private final ObjectMapper objectMapper;
	private final FetchAppointmentConsultationSummary fetchAppointmentConsultationSummary;

    @GetMapping(value = "/{institutionId}/monthly")
    public ResponseEntity<Resource> getMonthlyExcelReport(
            @PathVariable Integer institutionId,
            @RequestParam(value="fromDate", required = true) String fromDate,
            @RequestParam(value="toDate", required = true) String toDate,
            @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
            @RequestParam(value="doctorId", required = false) Integer doctorId,
			@RequestParam(value="hierarchicalUnitTypeId", required = false) Integer hierarchicalUnitTypeId,
			@RequestParam(value="hierarchicalUnitId", required = false) Integer hierarchicalUnitId,
			@RequestParam(value="includeHierarchicalUnitDescendants", required = false) boolean includeHierarchicalUnitDescendants
    ) throws Exception {
        log.debug("Se creará el excel {}", institutionId);
		log.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants);

        LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		var institutionMonthlyReportParams = ReportInstitutionQueryBo.builder()
				.institutionId(institutionId)
				.startDate(startDate)
				.endDate(endDate)
				.clinicalSpecialtyId(clinicalSpecialtyId)
				.doctorId(doctorId)
				.hierarchicalUnitTypeId(hierarchicalUnitTypeId)
				.hierarchicalUnitId(hierarchicalUnitId)
				.includeHierarchicalUnitDescendants(includeHierarchicalUnitDescendants)
				.build();


		return StoredFileResponse.sendFile(
				generateInstitutionMonthlyExcelReport.run(institutionMonthlyReportParams)
		);
    }

    @GetMapping(value = "/{institutionId}/summary")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR')")
    public ResponseEntity<Resource> fetchConsultationSummaryReport(
            @PathVariable Integer institutionId,
            @RequestParam(value="fromDate") String fromDate,
            @RequestParam(value="toDate") String toDate,
            @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
            @RequestParam(value="doctorId", required = false) Integer doctorId,
			@RequestParam(value="hierarchicalUnitTypeId", required = false) Integer hierarchicalUnitTypeId,
			@RequestParam(value="hierarchicalUnitId", required = false) Integer hierarchicalUnitId,
			@RequestParam(value="includeHierarchicalUnitDescendants", required = false) boolean includeHierarchicalUnitDescendants
		) {
		log.debug("Outpatient summary Report");
		log.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, clinicalSpecialtyId {}, " +
				"doctorId {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, includeHierarchicalUnitDescendants {}",
				institutionId, fromDate, toDate, clinicalSpecialtyId, doctorId, hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants);

        LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook workbook = consultationSummaryReport.build(institutionId, startDate, endDate, doctorId, clinicalSpecialtyId,
				hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants);
		String title = "Resumen Mensual de Consultorios Externos - Hoja 2.1";
        String filename = title + "." + workbook.getExtension();

		return StoredFileResponse.sendFile(
				buildReport(workbook),
				filename,
				workbook.getContentType()
		);
    }

	private FileContentBo buildReport(IWorkbook workbook) {
		return StreamsUtils.writeToContent((out) -> {
			try {
				workbook.write(out);
			} catch (Exception e) {
				throw streamException(e);
			}
		});
	}


    @GetMapping("/{institutionId}/appointment-annex")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Resource> getAppointmentAnnexReport(
            @PathVariable Integer institutionId,
            @RequestParam(name = "appointmentId") Integer appointmentId)
            throws PDFDocumentException {
		var report = sharedAppointmentAnnexPdfReportService.run(appointmentId) ;
		return StoredFileResponse.sendFile(
				report.getPdf(),
				report.getFilename(),
				MediaType.APPLICATION_PDF
		);
    }

    @GetMapping("/{institutionId}/consultations-annex")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<Resource> getConsultationAnnexReport(
            @PathVariable Integer institutionId,
            @RequestParam(name = "documentId") Long documentId)
            throws PDFDocumentException {
		log.debug("Input parameter -> documentId {}", documentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        AnnexIIBo reportDataBo = annexReportService.getConsultationData(new AnnexIIParametersBo(null, documentId));
        AnnexIIDto reportDataDto = reportsMapper.toAnexoIIDto(reportDataBo);
        Map<String, Object> context = annexReportService.createConsultationContext(reportDataDto);
		log.debug(OUTPUT, reportDataDto);

		setFlavor(context);

		return StoredFileResponse.sendFile(
				pdfService.generate("annex_report", context),
				annexReportService.createConsultationFileName(documentId, now),
				MediaType.APPLICATION_PDF
		);
    }

	private void setFlavor(Map<String, Object> context) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_ANEXO_II_MENDOZA))
			context.put("flavor", "mdz");
		else
			context.put("flavor", "pba");
	}

	@GetMapping("/{institutionId}/appointment-formv")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<Resource> getFormVAppointmentReport(
            @PathVariable Integer institutionId,
            @RequestParam(name = "appointmentId") Integer appointmentId)
            throws PDFDocumentException {
		log.debug("Input parameter -> appointmentId {}", appointmentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        FormVBo reportDataBo = formReportService.getAppointmentData(new FormVParametersBo(appointmentId, null));
        FormVDto reportDataDto = reportsMapper.toFormVDto(reportDataBo);
        Map<String, Object> context = formReportService.createAppointmentContext(reportDataDto);

		log.debug(OUTPUT, reportDataDto);

		return StoredFileResponse.sendFile(
				pdfService.generate("form_report", context),
				formReportService.createConsultationFileName(appointmentId.longValue(), now),
				MediaType.APPLICATION_PDF
		);

    }

    @GetMapping("/{institutionId}/consultation-formv")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
    public ResponseEntity<Resource> getFormVConsultationReport(
            @PathVariable Integer institutionId,
            @RequestParam(name = "documentId") Long documentId)
            throws PDFDocumentException {
		log.debug("Input parameter -> documentId {}", documentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        FormVBo reportDataBo = formReportService.getConsultationData(new FormVParametersBo(null, documentId));
        FormVDto reportDataDto = reportsMapper.toFormVDto(reportDataBo);
        Map<String, Object> context = formReportService.createConsultationContext(reportDataDto);

		return StoredFileResponse.sendFile(
				pdfService.generate("form_report", context),
				formReportService.createConsultationFileName(documentId, now),
				MediaType.APPLICATION_PDF
		);
    }



    @GetMapping("/institution/{institutionId}/patient/{patientId}/consultations-list")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<List<ConsultationsDto>> getConsultations(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId){
		log.debug("Input parameter -> patientId {}", patientId);
        List<ConsultationsBo> consultations = fetchConsultations.run(patientId);
		if (featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS)){
			consultations.stream().map(consultationsBo -> {
			if (consultationsBo.getCompleteProfessionalNameSelfDetermination() != null)
				consultationsBo.setCompleteProfessionalName(consultationsBo.getCompleteProfessionalNameSelfDetermination());
			return consultationsBo;
		}).collect(Collectors.toList());
		}
        List<ConsultationsDto> result = reportsMapper.fromListConsultationsBo(consultations);
        return ResponseEntity.ok(result);
    }

	@GetMapping("/institution/{institutionId}/diabetes")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getDiabetesReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/diabetesReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/hypertension")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getHypertensionReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/hypertensionReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/epidemiological_week")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getEpidemiologicalWeekReport(@PathVariable(name = "institutionId") Integer institutionId){
		log.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/epidemiologicalWeekReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/institution/{institutionId}/nominal-appointment-detail")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<Resource> getNominalAppointmentDetailReport(@PathVariable Integer institutionId,
																	  @RequestParam(value="fromDate") String fromDate,
																	  @RequestParam(value="toDate") String toDate,
																	  @RequestParam(value="clinicalSpecialtyId", required = false) Integer clinicalSpecialtyId,
																	  @RequestParam(value="doctorId", required = false) Integer doctorId,
																	  @RequestParam(value="hierarchicalUnitTypeId", required = false) Integer hierarchicalUnitTypeId,
																	  @RequestParam(value="hierarchicalUnitId", required = false) Integer hierarchicalUnitId,
																	  @RequestParam(value="appointmentStateId", required = false) Short appointmentStateId,
																	  @RequestParam(value="includeHierarchicalUnitDescendants", required = false) boolean includeHierarchicalUnitDescendants) throws Exception {
		log.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, appointmentStateId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, appointmentStateId, includeHierarchicalUnitDescendants);

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		var institutionMonthlyReportParams = ReportInstitutionQueryBo.builder()
				.institutionId(institutionId)
				.startDate(startDate)
				.endDate(endDate)
				.clinicalSpecialtyId(clinicalSpecialtyId)
				.doctorId(doctorId)
				.hierarchicalUnitTypeId(hierarchicalUnitTypeId)
				.hierarchicalUnitId(hierarchicalUnitId)
				.includeHierarchicalUnitDescendants(includeHierarchicalUnitDescendants)
				.appointmentStateId(appointmentStateId)
				.build();

		return StoredFileResponse.sendFile(
				generateAppointmentNominalDetailExcelReport.run(institutionMonthlyReportParams)
		);
	}

	@GetMapping(value = "/institution/{institutionId}/nominal-emergency-care-episode-detail")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<Resource> getNominalECEDetailReport(@PathVariable Integer institutionId,
															  @RequestParam(value="fromDate") String fromDate,
															  @RequestParam(value="toDate") String toDate,
															  @RequestParam(value="doctorId", required = false) Integer doctorId,
															  @RequestParam(value="hierarchicalUnitTypeId", required = false) Integer hierarchicalUnitTypeId,
															  @RequestParam(value="hierarchicalUnitId", required = false) Integer hierarchicalUnitId,
															  @RequestParam(value="includeHierarchicalUnitDescendants", required = false) boolean includeHierarchicalUnitDescendants) throws Exception {
		log.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants);

		if (!featureFlagsService.isOn(AppFeature.HABILITAR_REPORTE_DETALLE_NOMINAL_GUARDIA_EN_DESARROLLO))
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		var institutionMonthlyReportParams = ReportInstitutionQueryBo.builder()
				.institutionId(institutionId)
				.startDate(startDate)
				.endDate(endDate)
				.doctorId(doctorId)
				.hierarchicalUnitTypeId(hierarchicalUnitTypeId)
				.hierarchicalUnitId(hierarchicalUnitId)
				.includeHierarchicalUnitDescendants(includeHierarchicalUnitDescendants)
				.build();

		return StoredFileResponse.sendFile(
				generateEmergencyCareNominalDetailExcelReport.run(institutionMonthlyReportParams)
		);
	}

	@GetMapping(value = "/institution/{institutionId}/appointment-consultation-summary")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR, PERSONAL_DE_ESTADISTICA')")
	public ResponseEntity<Resource> getAppointmentConsultationSummaryReport(@PathVariable Integer institutionId,
																			@RequestParam String searchFilter) throws Exception {
		log.debug("Input parameters -> institutionId {}, searchFilter {}" , institutionId, searchFilter);
		String title = "Resumen Mensual de Turnos en Consultorios Externos ";
		IWorkbook wb = fetchAppointmentConsultationSummary.run(title, parseFilter(institutionId, searchFilter));
		String filename = title + "." + wb.getExtension();
		return StoredFileResponse.sendFile(
				buildReport(wb),
				filename,
				wb.getContentType()
		);
	}

	@GetMapping("/get-call-center-appointments")
	@PreAuthorize("hasAnyAuthority('GESTOR_CENTRO_LLAMADO')")
	public UIComponentDto getCallCenterAppointmentsReport() {
		return JsonResourceUtils.readJson("extension/reports/callCenterReport.json",
				new TypeReference<>() {},
				null
		);
	}

	private ReportSearchFilterBo parseFilter(Integer institutionId, String filter) {
		ReportSearchFilterBo searchFilter = null;
		try {
			searchFilter = this.objectMapper.readValue(filter, ReportSearchFilterBo.class);
			searchFilter.setInstitutionId(institutionId);
		} catch (IOException e) {
			log.error(String.format("Error mapping filter: %s", filter), e);
		}
		return searchFilter;
	}

}
