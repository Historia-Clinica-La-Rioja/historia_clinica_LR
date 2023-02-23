package ar.lamansys.refcounterref.infraestructure.input.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.refcounterref.domain.file.StoredFileBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reference-file")
public class ReferenceFileDownloadController {

    private static final String OUTPUT = "Output -> {}";
	private final GetReferenceFile getReferenceFile;


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

}
