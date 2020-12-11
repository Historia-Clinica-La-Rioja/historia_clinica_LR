package net.pladema.clinichistory.requests.medicalrequests.controller;

import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.medicalrequests.controller.dto.MedicalRequestDto;
import net.pladema.clinichistory.requests.medicalrequests.controller.dto.NewMedicalRequestDto;
import net.pladema.sgx.dates.controller.dto.DateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/institutions/{institutionId}/patient/{patientId}/medical-requests")
public class MedicalRequestController {
    private static final Logger LOG = LoggerFactory.getLogger(MedicalRequestController.class);

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Integer create(@PathVariable(name = "institutionId") Integer institutionId,
                   @PathVariable(name = "patientId") Integer patientId,
                   @RequestBody @Valid NewMedicalRequestDto medicalRequest) {
        LOG.debug("create -> institutionId {}, patientId {}, medicalRequest {}", institutionId, patientId, medicalRequest);
        Integer result = 12;
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
    @Transactional
    public void finalize(@PathVariable(name = "institutionId") Integer institutionId,
                         @PathVariable(name = "patientId") Integer patientId,
                         @PathVariable(name = "medicalRequestId") Integer medicalRequestId) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicalRequestId {}", institutionId, patientId, medicalRequestId);
        LOG.debug("suspend success");
    }

}
