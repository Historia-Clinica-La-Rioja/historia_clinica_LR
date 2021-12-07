package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.createcounterreferencefile.CreateCounterReferenceFile;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/counterreference-file")
@Api(value = "Counte Reference File", tags = {"Counter Reference File"})
public class CounterReferenceFileController {

    private static final String OUTPUT = "Output -> {}";

    private final CreateCounterReferenceFile createCounterReferenceFile;

    @PostMapping(value = "/patient/{patientId}/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public Integer uploadFile(@PathVariable(name = "institutionId") Integer institutionId,
                              @PathVariable(name = "patientId") Integer patientId,
                              @RequestPart("file") MultipartFile file) {
        log.debug("Input parameters -> {} institutionId {}, patientId {}", institutionId, patientId);
        var result = createCounterReferenceFile.run(institutionId, patientId, file);
        log.debug(OUTPUT, result);
        return result;
    }

}