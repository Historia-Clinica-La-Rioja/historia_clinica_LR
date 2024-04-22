package net.pladema.reports.controller;

import static ar.lamansys.sgx.shared.files.StreamsUtils.streamException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ar.lamansys.sgh.shared.application.annex.SharedAppointmentAnnexPdfReportService;

import com.fasterxml.jackson.core.type.TypeReference;

import net.pladema.hsi.extensions.infrastructure.controller.dto.UIComponentDto;
import net.pladema.hsi.extensions.utils.JsonResourceUtils;

import net.pladema.reports.application.fetchnominalconsultationdetail.FetchNominalConsultationDetail;
import net.pladema.reports.application.fetchnominalappointmentdetail.FetchNominalAppointmentDetail;

import net.pladema.reports.domain.NominalAppointmentDetailFiterlBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
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
import net.pladema.reports.repository.QueryFactory;
import net.pladema.reports.service.AnnexReportService;
import net.pladema.reports.service.NominalDetailExcelService;
import net.pladema.reports.service.FetchConsultations;
import net.pladema.reports.service.FormReportService;
import net.pladema.reports.service.domain.AnnexIIBo;
import net.pladema.reports.service.domain.ConsultationSummaryReport;
import net.pladema.reports.service.domain.ConsultationsBo;
import net.pladema.reports.service.domain.FormVBo;

@RestController
@RequestMapping("reports")
public class ReportsController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportsController.class);

    public static final String OUTPUT = "Output -> {}";

    private final NominalDetailExcelService nominalDetailExcelService;

    private final ConsultationSummaryReport consultationSummaryReport;

    private final QueryFactory queryFactory;

    private final LocalDateMapper localDateMapper;

    private final PdfService pdfService;

    private final AnnexReportService annexReportService;

    private final FormReportService formReportService;

    private final ReportsMapper reportsMapper;

    private final FetchConsultations fetchConsultations;

	private final FeatureFlagsService featureFlagsService;

	private final FetchNominalConsultationDetail fetchNominalConsultationDetail;

	private final FetchNominalAppointmentDetail fetchNominalAppointmentDetail;

	private final SharedAppointmentAnnexPdfReportService sharedAppointmentAnnexPdfReportService;

    public ReportsController(NominalDetailExcelService nominalDetailExcelService, ConsultationSummaryReport consultationSummaryReport,
							 QueryFactory queryFactory, LocalDateMapper localDateMapper,
							 PdfService pdfService, AnnexReportService annexReportService,
							 FormReportService formReportService, ReportsMapper reportsMapper,
							 FetchConsultations fetchConsultations, FeatureFlagsService featureFlagsService,
							 FetchNominalConsultationDetail fetchNominalConsultationDetail,
							 FetchNominalAppointmentDetail fetchNominalAppointmentDetail,
							 SharedAppointmentAnnexPdfReportService sharedAppointmentAnnexPdfReportService){
        this.nominalDetailExcelService = nominalDetailExcelService;
        this.consultationSummaryReport = consultationSummaryReport;
        this.queryFactory = queryFactory;
        this.localDateMapper = localDateMapper;
        this.pdfService = pdfService;
        this.annexReportService = annexReportService;
        this.formReportService = formReportService;
        this.reportsMapper = reportsMapper;
        this.fetchConsultations = fetchConsultations;
		this.featureFlagsService = featureFlagsService;
		this.fetchNominalAppointmentDetail = fetchNominalAppointmentDetail;
		this.fetchNominalConsultationDetail = fetchNominalConsultationDetail;
		this.sharedAppointmentAnnexPdfReportService = sharedAppointmentAnnexPdfReportService;
	}

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
        LOG.debug("Se creará el excel {}", institutionId);
        LOG.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants);

        String title = "DNCE-Hoja 2";
        String[] headers = new String[]{"Provincia", "Municipio", "Cod_Estable", "Establecimiento", "Tipo de unidad jerárquica", "Unidad jerárquica", "Apellidos paciente", "Nombres paciente", "Nombre autopercibido", "Tipo documento",
                "Nro documento", "Fecha de nacimiento", "Género autopercibido", "Domicilio", "Teléfono", "Mail", "Obra social/Prepaga", "Nro de afiliado",
                "Fecha de atención", "Especialidad", "Profesional", "Motivo de consulta", "Problemas de Salud / Diagnóstico", "Procedimientos", "Peso", "Talla", "Tensión sistólica",
				"Tensión diastólica", "Riesgo cardiovascular", "Hemoglobina glicosilada", "Glucemia", "Perímetro cefálico", "C-P-O", "c-e-o"};

        LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
        LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

        // obtengo el workbook en base a la query pasada como parametro
        IWorkbook wb = fetchNominalConsultationDetail.run(title, headers, this.queryFactory.query(institutionId, startDate, endDate,
				clinicalSpecialtyId, doctorId, hierarchicalUnitTypeId, hierarchicalUnitId, includeHierarchicalUnitDescendants));

        // armo la respuesta con el workbook obtenido
        String filename = title + "." + wb.getExtension();

		return StoredFileResponse.sendFile(
				buildReport(wb),
				filename,
				wb.getContentType()
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
        LOG.debug("Outpatient summary Report");
        LOG.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, clinicalSpecialtyId {}, " +
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
        LOG.debug("Input parameter -> documentId {}", documentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        AnnexIIBo reportDataBo = annexReportService.getConsultationData(documentId);
        AnnexIIDto reportDataDto = reportsMapper.toAnexoIIDto(reportDataBo);
        Map<String, Object> context = annexReportService.createConsultationContext(reportDataDto);
        LOG.debug(OUTPUT, reportDataDto);

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
        LOG.debug("Input parameter -> appointmentId {}", appointmentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        FormVBo reportDataBo = formReportService.getAppointmentData(appointmentId);
        FormVDto reportDataDto = reportsMapper.toFormVDto(reportDataBo);
        Map<String, Object> context = formReportService.createAppointmentContext(reportDataDto);

        LOG.debug(OUTPUT, reportDataDto);

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
        LOG.debug("Input parameter -> documentId {}", documentId);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        FormVBo reportDataBo = formReportService.getConsultationData(documentId);
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
        LOG.debug("Input parameter -> patientId {}", patientId);
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
 		LOG.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/diabetesReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/hypertension")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getHypertensionReport(@PathVariable(name = "institutionId") Integer institutionId){
		LOG.debug("Input parameter -> institutionId {}", institutionId);
		UIComponentDto result = JsonResourceUtils.readJson("extension/reports/hypertensionReport.json",
				new TypeReference<>() {},
				null
		);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/institution/{institutionId}/epidemiological_week")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, PERSONAL_DE_ESTADISTICA, ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE')")
	public ResponseEntity<UIComponentDto> getEpidemiologicalWeekReport(@PathVariable(name = "institutionId") Integer institutionId){
		LOG.debug("Input parameter -> institutionId {}", institutionId);
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
		LOG.debug("Input parameters -> institutionId {}, fromDate {}, toDate {}, hierarchicalUnitTypeId {}, hierarchicalUnitId {}, appointmentStateId {}, includeHierarchicalUnitDescendants {}" ,
				institutionId, fromDate, toDate, hierarchicalUnitTypeId, hierarchicalUnitId, appointmentStateId, includeHierarchicalUnitDescendants);

		String title = "DNT";

		LocalDate startDate = localDateMapper.fromStringToLocalDate(fromDate);
		LocalDate endDate = localDateMapper.fromStringToLocalDate(toDate);

		IWorkbook wb = fetchNominalAppointmentDetail.run(title, new NominalAppointmentDetailFiterlBo(institutionId, startDate, endDate, clinicalSpecialtyId,
				doctorId, hierarchicalUnitTypeId, hierarchicalUnitId, appointmentStateId, includeHierarchicalUnitDescendants));

		String filename = title + "." + wb.getExtension();

		return StoredFileResponse.sendFile(
				buildReport(wb),
				filename,
				wb.getContentType()
		);
	}
}
