package net.pladema.clinichistory.requests.servicerequests.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionItemDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.*;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.CreateServiceRequestMapper;
import net.pladema.clinichistory.requests.servicerequests.controller.mapper.StudyMapper;
import net.pladema.clinichistory.requests.servicerequests.service.CreateServiceRequestService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.ServiceRequestBo;
import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.sgx.security.utils.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    private static final String COMMON_INPUT = "Input parameters -> institutionId {} patientId {}, serviceRequestId {}";

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;
    private final CreateServiceRequestService createServiceRequestService;
    private final CreateServiceRequestMapper createServiceRequestMapper;
    private final PatientExternalService patientExternalService;
    private final StudyMapper studyMapper;
    public ServiceRequestController(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                    CreateServiceRequestService createServiceRequestService,
                                    CreateServiceRequestMapper createServiceRequestMapper,
                                    PatientExternalService patientExternalService,
                                    StudyMapper studyMapper) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createServiceRequestService = createServiceRequestService;
        this.createServiceRequestMapper = createServiceRequestMapper;
        this.patientExternalService = patientExternalService;
        this.studyMapper = studyMapper;
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

    @PutMapping("/{serviceRequestId}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public void complete(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "serviceRequestId") Integer serviceRequestId,
                         @RequestBody CompleteRequestDto completeRequestDto) {
        LOG.debug("Input parameters ->  {} institutionIdpatientId {}, serviceRequestId {}, completeRequestDto {}",
                institutionId,
                patientId,
                serviceRequestId,
                completeRequestDto);
    }

    @DeleteMapping("/{serviceRequestId}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable(name = "institutionId") Integer institutionId,
                       @PathVariable(name = "patientId") Integer patientId,
                       @PathVariable(name = "serviceRequestId") Integer serviceRequestId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, serviceRequestId);
    }

    @GetMapping("/{serviceRequestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public DiagnosticReportDto get(@PathVariable(name = "institutionId") Integer institutionId,
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

        DiagnosticReportDto result = new DiagnosticReportDto();
        result.setSnomed(snomed);
        result.setHealthCondition(healthCondition);
        result.setObservations("El paciente presenta la tiroides alta");
        result.setLink("http://www.google.com");
        result.setStatusId("123123");
        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping
    public List<DiagnosticReportDto> getList(@PathVariable(name = "institutionId") Integer institutionId,
                                             @PathVariable(name = "patientId") Integer patientId,
                                             @RequestParam(value = "statusId") Integer statusId,
                                             @RequestParam(value = "serviceRequest") String serviceRequest,
                                             @RequestParam(value = "healthCondition") String healthCondition) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, status {} serviceRequest {} healthCondition {}",
                institutionId,
                patientId,
                statusId,
                serviceRequest,
                healthCondition);

        SnomedDto snomed = new SnomedDto();
        snomed.setSctid("11111");
        snomed.setPt("Radiologia");

        SnomedDto healthConditionSnomed = new SnomedDto();
        snomed.setSctid("'2222'");
        snomed.setPt("'Anginas'");


        DiagnosticReportDto drDto1 = new DiagnosticReportDto(
                snomed,
                healthConditionSnomed,
                "Todo bien",
                "www.link.com",
                "123");

        DiagnosticReportDto drDto2 = new DiagnosticReportDto(
                snomed,
                healthConditionSnomed,
                "Todo mal",
                "www.link2.com",
                "193");

        List<DiagnosticReportDto> result = List.of(drDto1, drDto2);

        LOG.debug(OUTPUT, result);
        return result;
    }

}
