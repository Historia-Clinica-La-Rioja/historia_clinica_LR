package net.pladema.clinichistory.requests.medicationrequests.controller;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.clinichistory.requests.controller.dto.PrescriptionDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.DosageInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.HealthConditionInfoDto;
import net.pladema.clinichistory.requests.medicationrequests.controller.dto.MedicationInfoDto;
import net.pladema.sgx.dates.controller.dto.DateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/institutions/{institutionId}/patient/{patientId}/medication-requests")
@Api(value = "Medication Request", tags = {"Medication Request"})
@Validated
public class MedicationRequestController {

    private static final Logger LOG = LoggerFactory.getLogger(MedicationRequestController.class);

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @Transactional
    public @ResponseBody
    Integer create(@PathVariable(name = "institutionId") Integer institutionId,
                   @PathVariable(name = "patientId") Integer patientId,
                   @RequestBody @Valid PrescriptionDto medicationRequest) {
        LOG.debug("create -> institutionId {}, patientId {}, medicationRequest {}", institutionId, patientId, medicationRequest);
        Integer result = 12;
        LOG.debug("create result -> {}", result);
        return result;
    }

    @PutMapping(value = "/suspend")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void suspend(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "dayQuantity") Short dayQuantity,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, dayQuantity {}, medicationsIds {}", institutionId, patientId, dayQuantity, medicationsIds);
        LOG.debug("suspend success");
    }


    @PutMapping(value = "/finalize")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void finalize(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicationsIds {}", institutionId, patientId, medicationsIds);
        LOG.debug("suspend success");
    }

    @PutMapping(value = "/reactivate")
    @ResponseStatus(code = HttpStatus.OK)
    @Transactional
    public void reactivate(@PathVariable(name = "institutionId") Integer institutionId,
                        @PathVariable(name = "patientId") Integer patientId,
                        @RequestParam(value = "medicationsIds") List<Integer> medicationsIds) {
        LOG.debug("change-state -> institutionId {}, patientId {}, medicationsIds {}", institutionId, patientId, medicationsIds);
        LOG.debug("suspend success");
    }


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public @ResponseBody
    List<MedicationInfoDto> medicationRequestList(@PathVariable(name = "institutionId") Integer institutionId,
                                                  @PathVariable(name = "patientId") Integer patientId,
                                                  @RequestParam(value = "statusId", defaultValue = "active") String statusId,
                                                  @RequestParam(value = "medicationStatement", required = false) String medicationStatement,
                                                  @RequestParam(value = "healthCondition", required = false) String healthCondition) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, statusId {}, medicationStatement {}, healthCondition {}", institutionId, patientId, statusId, medicationStatement, healthCondition);
        List<MedicationInfoDto> result = List.of(
                new MedicationInfoDto(new SnomedDto("11111", "IBUPROFENO comprimido 600mg"), new HealthConditionInfoDto(6, new SnomedDto("222", "ANGINAS")), new DosageInfoDto(4, "horas", 4, "días"), "44444", true, new DateDto(2020, 5, 14),  "Tomarlo durante las mañanas en ayuno"),
                new MedicationInfoDto(new SnomedDto("333", "TAFIROL comprimido 1g"), new HealthConditionInfoDto(8,new SnomedDto("2222", "PAPERA")),new DosageInfoDto(1, "horas", null, "horas"),"44444", false,    new DateDto(2020, 5, 13),  "Tomar antes de las comidas"),
                new MedicationInfoDto(new SnomedDto("123", "PARACETAMOL comprimido 1g"), new HealthConditionInfoDto(8,new SnomedDto("2222", "PAPERA")), null, "44444", false,  new DateDto(2020, 5, 18), null),
                new MedicationInfoDto(new SnomedDto("777", "BAYASPIRINA comprimido 1g"), new HealthConditionInfoDto(8,new SnomedDto("2222", "PAPERA")),new DosageInfoDto(null, null, 8, "días"),"44444", true,  new DateDto(2020, 5, 13), null));
        LOG.debug("medicationRequestList result -> {}", result);
        return result;
    }

    @GetMapping(value = "/{medicationRequestId}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable(name = "institutionId") Integer institutionId,
                                                        @PathVariable(name = "patientId") Integer patientId,
                                                        @PathVariable(name = "medicationRequestId") Integer medicationRequestId) {
        LOG.debug("medicationRequestList -> institutionId {}, patientId {}, medicationRequestId {}", institutionId, patientId, medicationRequestId);
        String name = "MedicationRequest";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(os.toByteArray());
        InputStreamResource resource = new InputStreamResource(byteArrayInputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name)
                .contentType(MediaType.APPLICATION_PDF).contentLength(os.size()).body(resource);
    }

}