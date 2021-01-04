package net.pladema.clinichistory.requests.servicerequests.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.documents.service.domain.PatientInfoBo;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.*;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.CompleteDiagnosticReportMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.CreateServiceRequestMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.ListDiagnosticReportInfoMapper;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.DeleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private final ListDiagnosticReportInfoMapper listDiagnosticReportInfoMapper;
    private final CompleteDiagnosticReportService completeDiagnosticReportService;
    private final CompleteDiagnosticReportMapper completeDiagnosticReportMapper;

    public ServiceRequestController(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                    CreateServiceRequestService createServiceRequestService,
                                    CreateServiceRequestMapper createServiceRequestMapper,
                                    PatientExternalService patientExternalService,
                                    StudyMapper studyMapper,
                                    ListDiagnosticReportInfoMapper listDiagnosticReportInfoMapper,
                                    ListDiagnosticReportInfoService listDiagnosticReportInfoService,
                                    CompleteDiagnosticReportService completeDiagnosticReportService,
                                    CompleteDiagnosticReportMapper completeDiagnosticReportMapper) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createServiceRequestService = createServiceRequestService;
        this.createServiceRequestMapper = createServiceRequestMapper;
        this.patientExternalService = patientExternalService;
        this.listDiagnosticReportInfoService = listDiagnosticReportInfoService;
        this.listDiagnosticReportInfoMapper = listDiagnosticReportInfoMapper;
        this.studyMapper = studyMapper;
        this.completeDiagnosticReportService = completeDiagnosticReportService;
        this.completeDiagnosticReportMapper = completeDiagnosticReportMapper;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
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
            Integer srId = createServiceRequestService.execute(institutionId, serviceRequestBo);
            result.add(srId);
        });

        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping("/{serviceRequestId}/download")
    @ResponseStatus(code = HttpStatus.OK)
    public FileDto download(@PathVariable(name = "institutionId") Integer institutionId,
                            @PathVariable(name = "patientId") Integer patientId,
                            @PathVariable(name = "serviceRequestId") Integer serviceRequestId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, serviceRequestId);
        FileDto result = new FileDto();
        result.setFile("file");
        LOG.debug(OUTPUT, result);
        return result;
    }

    @PutMapping("/{diagnosticReportId}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public void complete(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId,
                         @RequestPart() CompleteRequestDto completeRequestDto,
                         @RequestPart(value = "file", required = false) MultipartFile file) {
        LOG.debug("Input parameters ->  {} institutionIdpatientId {}, diagnosticReportId {}, completeRequestDto {}",
                institutionId,
                patientId,
                diagnosticReportId,
                completeRequestDto);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        completeDiagnosticReportService.run(patientInfoBo, diagnosticReportId, completeDiagnosticReportMapper.parseTo(completeRequestDto), file);
    }

    @DeleteMapping("/{diagnosticReportId}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable(name = "institutionId") Integer institutionId,
                       @PathVariable(name = "patientId") Integer patientId,
                       @PathVariable(name = "diagnosticReportId") Integer diagnosticReportId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, diagnosticReportId);
        var patientDto = patientExternalService.getBasicDataFromPatient(patientId);
        PatientInfoBo patientInfoBo = new PatientInfoBo(patientDto.getId(), patientDto.getPerson().getGender().getId(), patientDto.getPerson().getAge());
        deleteDiagnosticReportService.execute(patientInfoBo, diagnosticReportId);
    }

    @GetMapping("/{serviceRequestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public DiagnosticReportInfoDto get(@PathVariable(name = "institutionId") Integer institutionId,
                                       @PathVariable(name = "patientId") Integer patientId,
                                       @PathVariable(name = "serviceRequestId") Integer serviceRequestId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, serviceRequestId);
        SnomedDto snomed = new SnomedDto();
        snomed.setSctid("11111");
        snomed.setPt("Radiologia");

        SnomedDto healthCondition = new SnomedDto();
        snomed.setSctid("'2222'");
        snomed.setPt("'ANGINAS'");
        HealthConditionInfoDto healthConditionInfo = new HealthConditionInfoDto();
        healthConditionInfo.setSnomed(healthCondition);

        DiagnosticReportInfoDto result = new DiagnosticReportInfoDto();
        result.setSnomed(snomed);
        result.setHealthCondition(healthConditionInfo);
        result.setObservations("El paciente presenta la tiroides alta");
        result.setLink("http://www.google.com");
        result.setStatusId("123123");
        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping
    public List<DiagnosticReportInfoDto> getList(@PathVariable(name = "institutionId") Integer institutionId,
                                                 @PathVariable(name = "patientId") Integer patientId,
                                                 @RequestParam(value = "status", required = false) String status,
                                                 @RequestParam(value = "diagnosticReport", required = false) String diagnosticReport,
                                                 @RequestParam(value = "healthCondition", required = false) String healthCondition) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, status {}, diagnosticReport {}, healthCondition {}",
                institutionId,
                patientId,
                status,
                diagnosticReport,
                healthCondition);

        List<DiagnosticReportBo> resultService = listDiagnosticReportInfoService.execute(new DiagnosticReportFilterBo(
                patientId,
                status,
                diagnosticReport,
                healthCondition));

        List<DiagnosticReportInfoDto> result = resultService.stream()
                .map(diagnosticReportBo -> {
                    ProfessionalDto professionalDto = healthcareProfessionalExternalService.findProfessionalByUserId(diagnosticReportBo.getUserId());
                    return listDiagnosticReportInfoMapper.parseTo(diagnosticReportBo, professionalDto);
                })
                .collect(Collectors.toList());

        LOG.trace(OUTPUT, result);
        return result;
    }

}
