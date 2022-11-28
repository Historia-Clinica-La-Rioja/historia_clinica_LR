package ar.lamansys.refcounterref.infraestructure.input.rest;

import ar.lamansys.refcounterref.application.createreferencefile.CreateReferenceFile;
import ar.lamansys.refcounterref.application.deletefiles.DeleteFiles;
import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.refcounterref.domain.file.StoredFileBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reference-file")
//@Api(value = "Reference File", tags = {"Reference File"})
public class ReferenceFileController {

    private static final String OUTPUT = "Output -> {}";

    private final CreateReferenceFile createReferenceFile;
    private final GetReferenceFile getReferenceFile;
    private final DeleteFiles deleteFiles;

    @PostMapping(value = "/patient/{patientId}/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
    public Integer uploadFile(@PathVariable(name = "institutionId") Integer institutionId,
                              @PathVariable(name = "patientId") Integer patientId,
                              @RequestPart("file") MultipartFile file) {
        log.debug("Input parameters -> institutionId {}, patientId {}", institutionId, patientId);
        var result = createReferenceFile.run(institutionId, patientId, file);
        log.debug(OUTPUT, result);
        return result;
    }

    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO')")
    public ResponseEntity download(@PathVariable(name = "institutionId") Integer institutionId,
                                   @PathVariable(name = "fileId") Integer fileId) {
        log.debug("Input parameters -> institutionId {}, fileId {}", institutionId, fileId);
        StoredFileBo result = getReferenceFile.run(fileId);
        log.debug(OUTPUT, result);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(result.getContentType()))
                .contentLength(result.getContentLenght())
                .body(result.getResource());
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
