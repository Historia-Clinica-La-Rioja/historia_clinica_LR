package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.createreferencefile.CreateReferenceFile;
import ar.lamansys.refcounterref.application.deletefiles.DeleteFiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reference-file")
//@Api(value = "Reference File", tags = {"Reference File"})
public class ReferenceFileController {

    private static final String OUTPUT = "Output -> {}";

    private final CreateReferenceFile createReferenceFile;
    private final DeleteFiles deleteFiles;

    @PostMapping(value = "/patient/{patientId}/uploadFiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public List<Integer> uploadFile(@PathVariable(name = "institutionId") Integer institutionId,
                              @PathVariable(name = "patientId") Integer patientId,
                              @RequestPart("files") MultipartFile[] files) throws IOException {
        log.debug("Input parameters -> institutionId {}, patientId {}, files {}", institutionId, patientId, files);
        var result = createReferenceFile.run(institutionId, patientId, files);
        log.debug(OUTPUT, result);
        return result;
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public boolean delete(@PathVariable(name = "institutionId") Integer institutionId,
                          @RequestParam(name = "fileIds") List<Integer> fileIds) {
        log.debug("Input parameters -> institutionId {}, fileIds {}", institutionId, fileIds);
        deleteFiles.run(fileIds);
        return true;
    }
}
