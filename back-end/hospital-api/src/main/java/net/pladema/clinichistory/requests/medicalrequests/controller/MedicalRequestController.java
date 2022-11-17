package net.pladema.clinichistory.requests.medicalrequests.controller;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.requests.medicalrequests.controller.dto.MedicalRequestDto;
import net.pladema.clinichistory.requests.medicalrequests.controller.dto.NewMedicalRequestDto;
import net.pladema.clinichistory.requests.medicalrequests.controller.mapper.CreateMedicalRequestMapper;
import net.pladema.clinichistory.requests.medicalrequests.service.CreateMedicalRequestService;
import net.pladema.clinichistory.requests.medicalrequests.service.domain.MedicalRequestBo;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/institutions/{institutionId}/patient/{patientId}/medical-requests")
@Tag(name = "Medical Request", description = "Medical Request")
public class MedicalRequestController {
    private static final Logger LOG = LoggerFactory.getLogger(MedicalRequestController.class);

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final CreateMedicalRequestMapper createMedicalRequestMapper;

    private final CreateMedicalRequestService createMedicalRequestService;

    public MedicalRequestController(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                                    CreateMedicalRequestMapper createMedicalRequestMapper,
                                    CreateMedicalRequestService createMedicalRequestService) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.createMedicalRequestMapper = createMedicalRequestMapper;
        this.createMedicalRequestService = createMedicalRequestService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public @ResponseBody
    Integer create(@PathVariable(name = "institutionId") Integer institutionId,
                   @PathVariable(name = "patientId") Integer patientId,
                   @RequestBody @Valid NewMedicalRequestDto medicalRequest) {
        LOG.debug("create -> institutionId {}, patientId {}, medicalRequest {}", institutionId, patientId, medicalRequest);
        MedicalRequestBo medicalRequestBo = createMedicalRequestMapper.toMedicalRequestBo(medicalRequest);
        Integer doctorId = healthcareProfessionalExternalService.getProfessionalId(UserInfo.getCurrentAuditor());
        medicalRequestBo.setDoctorId(doctorId);
        medicalRequestBo.setPatientId(patientId);
        Integer result = createMedicalRequestService.execute(institutionId, medicalRequestBo);
        LOG.debug("create result -> {}", result);
        return result;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public @ResponseBody
    List<MedicalRequestDto> medicalRequestList(@PathVariable(name = "institutionId") Integer institutionId,
                                                  @PathVariable(name = "patientId") Integer patientId) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}", institutionId, patientId);
        List<MedicalRequestDto> result = List.of(
                new MedicalRequestDto("Comer verduras",
                        4444,
                        new SnomedDto("2222", "ANGINAS"),
                        new DateDto(2020, 5 , 14))
        );
        LOG.debug("medicalRequestList result -> {}", result);
        return result;
    }

    @PutMapping(value = "/{medicalRequestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void finalize(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "medicalRequestId") Integer medicalRequestId) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicalRequestId {}", institutionId, patientId, medicalRequestId);
        LOG.debug("suspend success");
    }

}
