package net.pladema.clinichistory.requests.servicerequests.controller;

import io.swagger.annotations.Api;
import ar.lamansys.sgh.clinichistory.domain.document.PatientInfoBo;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.*;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.*;
import net.pladema.clinichistory.requests.servicerequests.service.*;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.StoredFileBo;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.service.PatientExternalMedicalCoverageService;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
import ar.lamansys.sgx.shared.pdf.PDFDocumentException;
import ar.lamansys.sgx.shared.pdf.PdfService;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(value = "Service Request", tags = {"Service Request"})
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/service-requests")
public class ServiceRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);
    private static final String OUTPUT = "create result -> {}";
    private static final String COMMON_INPUT = "Input parameters -> institutionId {} patientId {}, diagnosticReportId {}";

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final CreateServiceRequestService createServiceRequestService;
    private final CreateServiceRequestMapper createServiceRequestMapper;
    private final PatientExternalService patientExternalService;
    private final StudyMapper studyMapper;
    private final ListDiagnosticReportInfoService listDiagnosticReportInfoService;
    private final DiagnosticReportInfoMapper diagnosticReportInfoMapper;
    private final DeleteDiagnosticReportService deleteDiagnosticReportService;
    private final CompleteDiagnosticReportService completeDiagnosticReportService;
    private final CompleteDiagnosticReportMapper completeDiagnosticReportMapper;
    private final UploadDiagnosticReportCompletedFileService uploadDiagnosticReportCompletedFileService;
    private final UpdateDiagnosticReportFileService updateDiagnosticReportFileService;
    private final DiagnosticReportInfoService diagnosticReportInfoService;
    private final FileMapper fileMapper;
    private final ServeDiagnosticReportFileService serveDiagnosticReportFileService;
    private final PatientExternalMedicalCoverageService patientExternalMedicalCoverageService;
    private final PdfService pdfService;
    private final GetServiceRequestInfoService getServiceRequestInfoService;

    public ServiceRequestController(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                    CreateServiceRequestService createServiceRequestService,
                                    CreateServiceRequestMapper createServiceRequestMapper,
                                    PatientExternalService patientExternalService,
                                    StudyMapper studyMapper,
                                    DiagnosticReportInfoMapper diagnosticReportInfoMapper,
                                    ListDiagnosticReportInfoService listDiagnosticReportInfoService,
                                    DeleteDiagnosticReportService deleteDiagnosticReportService, CompleteDiagnosticReportService completeDiagnosticReportService,
                                    CompleteDiagnosticReportMapper completeDiagnosticReportMapper,
                                    UploadDiagnosticReportCompletedFileService uploadDiagnosticReportCompletedFileService,
                                    UpdateDiagnosticReportFileService updateDiagnosticReportFileService, DiagnosticReportInfoService diagnosticReportInfoService, FileMapper fileMapper, ServeDiagnosticReportFileService serveDiagnosticReportFileService, PatientExternalMedicalCoverageService patientExternalMedicalCoverageService, PdfService pdfService, GetServiceRequestInfoService getServiceRequestInfoService) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createServiceRequestService = createServiceRequestService;
        this.createServiceRequestMapper = createServiceRequestMapper;
        this.patientExternalService = patientExternalService;
        this.listDiagnosticReportInfoService = listDiagnosticReportInfoService;
        this.diagnosticReportInfoMapper = diagnosticReportInfoMapper;
        this.studyMapper = studyMapper;
        this.deleteDiagnosticReportService = deleteDiagnosticReportService;
        this.completeDiagnosticReportService = completeDiagnosticReportService;
        this.completeDiagnosticReportMapper = completeDiagnosticReportMapper;
        this.uploadDiagnosticReportCompletedFileService = uploadDiagnosticReportCompletedFileService;
        this.updateDiagnosticReportFileService = updateDiagnosticReportFileService;
        this.diagnosticReportInfoService = diagnosticReportInfoService;
        this.fileMapper = fileMapper;
        this.serveDiagnosticReportFileService = serveDiagnosticReportFileService;
        this.patientExternalMedicalCoverageService = patientExternalMedicalCoverageService;
        this.pdfService = pdfService;
        this.getServiceRequestInfoService = getServiceRequestInfoService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<Integer> create(@PathVariable(name = "institutionId") Integer institutionId,
                                @PathVariable(name = "patientId") Integer patientId,
                                @RequestBody @Valid PrescriptionDto serviceRequestListDto
    ) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, ServiceRequestListDto {}", institutionId, patientId, serviceRequestListDto);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        Map<String, List<PrescriptionItemDto>> srGroupBy = serviceRequestListDto.getItems().stream()
                .collect(Collectors.groupingBy(PrescriptionItemDto::getCategoryId));

        BasicPatientDto patientDto = patientExternalService.getBasicDataFromPatient(patientId);

        ArrayList<Integer> result = new ArrayList<>();

        srGroupBy.forEach((categoryId, studyListDto) -> {
            ServiceRequestBo serviceRequestBo = createServiceRequestMapper.parseTo(
                    studyMapper,
                    doctorId,
                    patientDto,
                    categoryId,
                    serviceRequestListDto.getMedicalCoverageId(),
                    studyListDto);
            serviceRequestBo.setInstitutionId(institutionId);
            Integer srId = createServiceRequestService.execute(serviceRequestBo);
            result.add(srId);
        });

        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity download(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "patientId") Integer patientId,
                                   @PathVariable(name = "fileId") Integer fileId
    ) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, fileId {}", institutionId, patientId, fileId);
        StoredFileBo result = serveDiagnosticReportFileService.run(fileId);
        LOG.debug(OUTPUT, result);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .contentLength(result.getContentLenght())
                .body(result.getResource());
    }

    @PutMapping("/{diagnosticReportId}/complete")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void complete(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
                         @RequestBody() CompleteRequestDto completeRequestDto) {
        LOG.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}, completeRequestDto {}",
                institutionId,
                patientId,
                diagnosticReportId,
                completeRequestDto);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        Integer result = completeDiagnosticReportService.run(patientInfoBo, diagnosticReportId, completeDiagnosticReportMapper.parseTo(completeRequestDto));
        updateDiagnosticReportFileService.run(result, completeRequestDto.getFileIds());
        LOG.debug(OUTPUT, result);
    }

    @PostMapping(value = "/{diagnosticReportId}/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<Integer> uploadFile(@PathVariable(name = "institutionId") Integer institutionId,
                                    @PathVariable(name = "patientId") Integer patientId,
                                    @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
                                    @RequestPart("files") MultipartFile[] files) {
        LOG.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}",
                institutionId,
                patientId,
                diagnosticReportId);
        var result = uploadDiagnosticReportCompletedFileService.execute(files, diagnosticReportId, patientId);
        LOG.debug(OUTPUT, result);
        return result;
    }


    @DeleteMapping("/{diagnosticReportId}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ESPECIALISTA_EN_ODONTOLOGIA')")
    public void delete(@PathVariable(name = "institutionId") Integer institutionId,
                       @PathVariable(name = "patientId") Integer patientId,
                       @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        deleteDiagnosticReportService.execute(patientInfoBo, diagnosticReportId);
    }

    @GetMapping("/{diagnosticReportId}")
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public DiagnosticReportInfoWithFilesDto get(@PathVariable(name = "institutionId") Integer institutionId,
                                                @PathVariable(name = "patientId") Integer patientId,
                                                @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);


        DiagnosticReportBo resultService = diagnosticReportInfoService.run(diagnosticReportId);
        ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(resultService.getUserId());
        DiagnosticReportInfoDto driDto = diagnosticReportInfoMapper.parseTo(resultService, professionalDto);
        DiagnosticReportInfoWithFilesDto result = new DiagnosticReportInfoWithFilesDto(
                driDto,
                fileMapper.parseToList(resultService.getFiles()));
        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public List<DiagnosticReportInfoDto> getList(@PathVariable(name = "institutionId") Integer institutionId,
                                                 @PathVariable(name = "patientId") Integer patientId,
                                                 @RequestParam(value = "statusId", required = false) String status,
                                                 @RequestParam(value = "study", required = false) String study,
                                                 @RequestParam(value = "category", required = false) String category,
                                                 @RequestParam(value = "healthCondition", required = false) String healthCondition) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, status {}, diagnosticReport {}, healthCondition {}, categpry {}",
                institutionId,
                patientId,
                status,
                study,
                healthCondition,
                category);

        List<DiagnosticReportBo> resultService = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(
                patientId,
                status,
                study,
                healthCondition,
                category));

        List<DiagnosticReportInfoDto> result = resultService.stream()
                .map(diagnosticReportBo -> {
                    ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(diagnosticReportBo.getUserId());
                    return diagnosticReportInfoMapper.parseTo(diagnosticReportBo, professionalDto);
                })
                .collect(Collectors.toList());

        LOG.trace(OUTPUT, result);
        return result;
    }


    @GetMapping(value = "/{serviceRequestId}/download-pdf")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable(name = "institutionId") Integer institutionId,
                                                           @PathVariable(name = "patientId") Integer patientId,
                                                           @PathVariable(name = "serviceRequestId") Integer serviceRequestId) throws PDFDocumentException {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, serviceRequestId {}", institutionId, patientId, serviceRequestId);
        var serviceRequestBo = getServiceRequestInfoService.run(serviceRequestId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        var professionalDto = healthcareProfessionalExternalService.findProfessionalById(serviceRequestBo.getDoctorId());
        var patientCoverageDto = patientExternalMedicalCoverageService.getCoverage(serviceRequestBo.getMedicalCoverageId());
        var context = createContext(serviceRequestBo, patientDto, professionalDto, patientCoverageDto);

        String template = "recipe_order_table";

        ByteArrayOutputStream os = pdfService.writer(template, context);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(os.size())
                .body(resource);
    }

    private Map<String, Object> createContext(ServiceRequestBo serviceRequestBo,
                                              BasicPatientDto patientDto,
                                              ProfessionalDto professionalDto,
                                              PatientMedicalCoverageDto patientCoverageDto) {
        LOG.debug("Input parameters -> serviceRequestBo {}, patientDto {}, professionalDto {}, patientCoverageDto {}",
                serviceRequestBo, patientDto, professionalDto, patientCoverageDto);
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("recipe", false);
        ctx.put("order", true);
        ctx.put("request", serviceRequestBo);
        ctx.put("patient", patientDto);
        ctx.put("professional", professionalDto);
        ctx.put("patientCoverage", patientCoverageDto);
        var date = serviceRequestBo.getRequestDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        ctx.put("requestDate", date);
        LOG.debug("Output -> {}", ctx);

        return ctx;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ IllegalArgumentException.class })
    public ApiErrorDto handleValidationExceptions(IllegalArgumentException ex) {
        LOG.error("Constraint violation -> {}", ex.getMessage());
        return new ApiErrorDto("Constraint violation", ex.getMessage());
    }

}
