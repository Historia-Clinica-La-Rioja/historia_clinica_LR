package net.pladema.clinichistory.outpatient.createoutpatient.controller;

import com.itextpdf.text.DocumentException;
import io.swagger.annotations.Api;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.dto.CreateOutpatientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;

@Api(value = "Outpatient consultations", tags = { "Outpatient consultations" })
public interface OutpatientConsultationAPI {

    @PostMapping("/billable")
    ResponseEntity<Boolean> createOutpatientConsultation(
            @PathVariable(name = "institutionId") Integer institutionId,
            @PathVariable(name = "patientId") Integer patientId,
            @RequestBody @Valid CreateOutpatientDto createOutpatientDto) throws IOException, DocumentException;
}
