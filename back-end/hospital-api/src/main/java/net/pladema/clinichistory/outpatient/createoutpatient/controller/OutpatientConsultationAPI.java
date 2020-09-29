package net.pladema.clinichistory.outpatient.createoutpatient.controller;


import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import net.pladema.clinichistory.ips.controller.dto.HealthConditionNewConsultationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.HasAppointment;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientImmunizationDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.OutpatientUpdateImmunizationDto;
import net.pladema.sgx.pdf.PDFDocumentException;

@Api(value = "Outpatient consultations", tags = { "Outpatient consultations" })
public interface OutpatientConsultationAPI {

    @PostMapping("/billable")
    ResponseEntity<Boolean> createOutpatientConsultation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") @HasAppointment Integer patientId,
            @RequestBody @Valid CreateOutpatientDto createOutpatientDto) throws IOException, PDFDocumentException;

    @PostMapping("/gettingVaccine")
    ResponseEntity<Boolean> gettingVaccine(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") @HasAppointment Integer patientId,
            @RequestParam(name = "finishAppointment") Boolean finishAppointment,
            @RequestBody @Valid OutpatientImmunizationDto vaccineDto) throws IOException, PDFDocumentException;

    @PostMapping("/updateImmunization")
    ResponseEntity<Boolean> updateImmunization(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid OutpatientUpdateImmunizationDto vaccineDto) throws IOException, PDFDocumentException;

    @PostMapping("/solveProblem")
    ResponseEntity<Boolean> solveHealthCondition(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid HealthConditionNewConsultationDto solvedProblemDto) throws IOException, PDFDocumentException;
}
