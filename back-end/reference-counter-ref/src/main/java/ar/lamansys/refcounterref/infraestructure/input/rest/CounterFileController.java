package ar.lamansys.refcounterref.infraestructure.input.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ar.lamansys.refcounterref.application.createcounterreferencefile.CreateCounterReferenceFile;
import ar.lamansys.refcounterref.application.deletefiles.DeleteFiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/counterreference-file")
public class CounterFileController {

    private static final String OUTPUT = "Output -> {}";

    private final CreateCounterReferenceFile createCounterReferenceFile;
    private final DeleteFiles deleteFiles;

    @PostMapping(value = "/patient/{patientId}/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, GESTOR_DE_ACCESO_INSTITUCIONAL') || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
	public Integer uploadFiles(@PathVariable(name = "institutionId") Integer institutionId,
                              @PathVariable(name = "patientId") Integer patientId,
                              @RequestPart("file") MultipartFile file) throws IOException {
        log.debug("Input parameters -> {} institutionId {}, patientId {}", institutionId, patientId);
        var result = createCounterReferenceFile.run(institutionId, patientId, file);
        log.debug(OUTPUT, result);
        return result;
    }

    @DeleteMapping("/delete")
	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, GESTOR_DE_ACCESO_INSTITUCIONAL') || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
    public boolean delete(@PathVariable(name = "institutionId") Integer institutionId,
                          @RequestParam(name = "fileIds") List<Integer> fileIds) {
        log.debug("Input parameters -> institutionId {}, fileIds {}", institutionId, fileIds);
        deleteFiles.run(fileIds);
        return true;
    }

}