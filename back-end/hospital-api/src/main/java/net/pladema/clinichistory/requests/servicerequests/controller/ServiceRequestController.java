package net.pladema.clinichistory.requests.servicerequests.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.DiagnosticReportDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.FileDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.NewServiceRequestListDto;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.servicerequests.controller.dto.CompleteRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "Service Request", tags = {"Service Request"})
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/service-requests")
public class ServiceRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceRequestController.class);
    private static final String OUTPUT = "create result -> {}";
    private static final String COMMON_INPUT = "Input parameters -> institutionId {} patientId {}, serviceRequestId {}";


    @PostMapping
    @Transactional
    public Integer newServiceRequest(@PathVariable(name = "institutionId") Integer institutionId,
                                                     @PathVariable(name = "patientId") Integer patientId,
                                                     @RequestBody NewServiceRequestListDto serviceRequestListDto
    ) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, ServiceRequestListDto {}", institutionId, patientId, serviceRequestListDto);
        Integer result = 8;
        LOG.debug(OUTPUT, result);
        return result;
    }

    @GetMapping("/{serviceRequestId}/download")
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
    public DiagnosticReportDto get(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "patientId") Integer patientId,
                                   @PathVariable(name = "serviceRequestId") Integer serviceRequestId
    ) {
        LOG.debug(COMMON_INPUT, institutionId, patientId, serviceRequestId);
        SnomedDto snomed = new SnomedDto();
        snomed.setId("11111");
        snomed.setPt("Radiologia");

        SnomedDto healthCondition = new SnomedDto();
        snomed.setId("'2222'");
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
                                             @RequestParam(value = "status") Integer status,
                                             @RequestParam(value = "serviceRequest") String serviceRequest,
                                             @RequestParam(value = "healthCondition") String healthCondition) {
        LOG.debug("Input parameters -> institutionId {} patientId {}, status {} serviceRequest {} healthCondition {}",
                institutionId,
                patientId,
                status,
                serviceRequest,
                healthCondition);

        SnomedDto snomed = new SnomedDto();
        snomed.setId("11111");
        snomed.setPt("Radiologia");

        SnomedDto healthConditionSnomed = new SnomedDto();
        snomed.setId("'2222'");
        snomed.setPt("'ANGINAS'");


        DiagnosticReportDto drDto1 = new DiagnosticReportDto(snomed,
                healthConditionSnomed,
                "Todo bien",
                "www.link.com",
                "123");

        DiagnosticReportDto drDto2 = new DiagnosticReportDto(snomed,
                healthConditionSnomed,
                "Todo mal",
                "www.link2.com",
                "193");

        List<DiagnosticReportDto> result = List.of(drDto1, drDto2);

        LOG.debug(OUTPUT, result);
        return result;
    }

}
