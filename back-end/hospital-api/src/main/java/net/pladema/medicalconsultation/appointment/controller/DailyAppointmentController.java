package net.pladema.medicalconsultation.appointment.controller;

import io.swagger.annotations.Api;
import net.pladema.medicalconsultation.appointment.controller.dto.AttentionTypeReportDto;
import net.pladema.medicalconsultation.appointment.controller.dto.AttentionTypeReportItemDto;
import net.pladema.medicalconsultation.appointment.service.DailyAppointmentReport;
import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportBo;
import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportItemBo;
import net.pladema.medicalconsultation.diary.service.DiaryService;
import net.pladema.medicalconsultation.diary.service.domain.DiaryBo;
import net.pladema.medicalconsultation.repository.entity.MedicalAttentionType;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;
import net.pladema.sgx.dates.configuration.LocalDateMapper;
import net.pladema.sgx.pdf.PDFDocumentException;
import net.pladema.sgx.pdf.PdfService;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/institutions/{institutionId}/medicalConsultations/daily-appointments-report")
@Api(value = "Daily appointments report", tags = { "Daily appointments report" })
public class DailyAppointmentController {

    private static final Logger LOG = LoggerFactory.getLogger(DailyAppointmentController.class);

    public static final String OUTPUT = "Output -> {}";

    private final LocalDateMapper localDateMapper;

    private final DailyAppointmentReport dailyAppointmentReport;

    private final PatientExternalService patientExternalService;

    private final DiaryService diaryService;

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final PdfService pdfService;

    public DailyAppointmentController(DailyAppointmentReport dailyAppointmentReport,
                                      LocalDateMapper localDateMapper,
                                      PatientExternalService patientExternalService,
                                      DiaryService diaryService, HealthcareProfessionalExternalService healthcareProfessionalExternalService, PdfService pdfService){
        this.dailyAppointmentReport = dailyAppointmentReport;
        this.localDateMapper = localDateMapper;
        this.patientExternalService = patientExternalService;
        this.diaryService = diaryService;
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    @PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO, ADMINISTRADOR_AGENDA')")
    public ResponseEntity<InputStreamResource> getDailyAppointmentsByDiaryIdAndDate(@PathVariable(name = "institutionId") Integer institutionId,
                                                                                    @RequestParam(name = "diaryId") Integer diaryId,
                                                                                    @RequestParam(name = "date") String date)
                                                            throws PDFDocumentException {
        LOG.debug("Input parameters -> institutionId {}, diaryId {}, date {}", institutionId, diaryId, date);
        LocalDate consultedDate = localDateMapper.fromStringToLocalDate(date);
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
        List<AttentionTypeReportBo> dailyAppointments = dailyAppointmentReport.execute(institutionId, diaryId, consultedDate);
        List<AttentionTypeReportDto> attentionTypeReportDtos = createPatientAssociatedReportList(dailyAppointments);
        Integer healthCareProfessionalId = diaryService.getDiary(diaryId).map(DiaryBo::getHealthcareProfessionalId).orElse(null);
        ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalById(healthCareProfessionalId);
        Map<String, Object> context = createContext(professionalDto, attentionTypeReportDtos, consultedDate, now);
        String outputFileName = createOutputFileName(professionalDto, consultedDate);
        ResponseEntity<InputStreamResource> response = generatePdfResponse(context, outputFileName);
        LOG.debug(OUTPUT, response);
        return response;
    }

    private ResponseEntity<InputStreamResource> generatePdfResponse(Map<String, Object> context, String outputFileName) throws PDFDocumentException {
        LOG.debug("Input parameters -> context {}, outputFileName {}", context, outputFileName);
        ByteArrayOutputStream outputStream = pdfService.writer("daily-appointments", context);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        ResponseEntity<InputStreamResource> response;
        response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + outputFileName)
                .contentType(MediaType.APPLICATION_PDF).contentLength(outputStream.size()).body(resource);
        return response;
    }


    private List<AttentionTypeReportDto> createPatientAssociatedReportList(List<AttentionTypeReportBo> attentionTypeReportBos) {
        LOG.debug("Input parameters -> attentionTypeReportBos {}", attentionTypeReportBos);
        List<AttentionTypeReportDto> result = attentionTypeReportBos.stream()
                .map(this::associateAttentionTypeWithPatientData)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, result);
        return result;
    }

    private AttentionTypeReportDto associateAttentionTypeWithPatientData(AttentionTypeReportBo attentionTypeReportBo) {
        LOG.debug("Input parameters -> attentionTypeReportBo {}", attentionTypeReportBo);
        List<AttentionTypeReportItemDto> reportItems = createPatientAssociatedReportItemList(attentionTypeReportBo);
        AttentionTypeReportDto result = new AttentionTypeReportDto(attentionTypeReportBo.getMedicalAttentionTypeId(),
                attentionTypeReportBo.getOpeningHourFrom(),
                attentionTypeReportBo.getOpeningHourTo(),
                reportItems);
        LOG.debug(OUTPUT, result);
        return result;
    }

    private List<AttentionTypeReportItemDto> createPatientAssociatedReportItemList(AttentionTypeReportBo attentionTypeReportBo) {
        LOG.debug("Input parameters -> attentionTypeReportBo {}", attentionTypeReportBo);
        List<AttentionTypeReportItemDto> newReportItemList = attentionTypeReportBo.getAppointments().stream()
                .map(this::createPatientAssociatedReportItem)
                .collect(Collectors.toList());
        LOG.debug(OUTPUT, newReportItemList);
        return newReportItemList;
    }

    private AttentionTypeReportItemDto createPatientAssociatedReportItem(AttentionTypeReportItemBo attentionTypeReportItemBo){
        LOG.debug("Input parameters -> attentionTypeReportItemBo {}", attentionTypeReportItemBo);
        BasicDataPersonDto personData = patientExternalService.getBasicDataFromPatient(attentionTypeReportItemBo.getPatientId()).getPerson();
        AttentionTypeReportItemDto newReportItem = new AttentionTypeReportItemDto(attentionTypeReportItemBo, personData);
        if (attentionTypeReportItemBo.getPatientMedicalCoverageId() != null){
            PatientMedicalCoverageDto patientMedicalCoverageDto = patientExternalService.getCoverage(attentionTypeReportItemBo.getPatientMedicalCoverageId());
            newReportItem.setMedicalCoverageName(patientMedicalCoverageDto.getMedicalCoverage().getName());
            newReportItem.setMedicalCoverageAffiliateNumber(patientMedicalCoverageDto.getAffiliateNumber());
        }
        LOG.debug(OUTPUT, newReportItem);
        return newReportItem;
    }

    private Map<String, Object> createContext(ProfessionalDto professionalDto, List<AttentionTypeReportDto> attentionTypeReportDtos,
                                              LocalDate consultedDate,
                                              ZonedDateTime actualDateTime){
        LOG.debug("Input parameters -> professionalDto {}, attentionTypeReportDtos {}, consultedDate {}, actualDateTime {}",
                professionalDto, attentionTypeReportDtos, consultedDate, actualDateTime);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("professionalName", professionalDto.getCompleteName());
        ctx.put("consultedDate", consultedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ctx.put("actualDateTime", actualDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        ctx.put("attentionTypes", attentionTypeReportDtos);
        ctx.put("PROGRAMMED_ATTENTION_TYPE", MedicalAttentionType.PROGRAMMED);
        ctx.put("SPONTANEOUS_ATTENTION_TYPE", MedicalAttentionType.SPONTANEOUS);
        LOG.debug(OUTPUT, ctx);
        return ctx;
    }

    private String createOutputFileName(ProfessionalDto professionalDto, LocalDate consultedDate){
        LOG.debug("Input parameters -> professionalDto {}, consultedDate {}", professionalDto, consultedDate);
        String formattedDate = consultedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String outputFileName = String.format("%s. Turnos %s", professionalDto.getCompleteName(), formattedDate);
        LOG.debug(OUTPUT, outputFileName);
        return outputFileName;
    }
}
