package ar.lamansys.refcounterref.infraestructure.input.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.refcounterref.application.getreferencefile.GetReferenceFile;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/institutions/{institutionId}/reference-file")
public class ReferenceFileDownloadController {
	private final GetReferenceFile getReferenceFile;


    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA, ENFERMERO, ADMINISTRATIVO') || " +
			"hasAnyAuthority('GESTOR_DE_ACCESO_DE_DOMINIO', 'GESTOR_DE_ACCESO_REGIONAL', 'GESTOR_DE_ACCESO_LOCAL')")
    public ResponseEntity<Resource> download(@PathVariable(name = "institutionId") Integer institutionId,
											 @PathVariable(name = "fileId") Integer fileId) {
        log.debug("Input parameters -> institutionId {}, fileId {}", institutionId, fileId);
		return StoredFileResponse.sendFile(
				getReferenceFile.run(fileId)
		);
    }

}
