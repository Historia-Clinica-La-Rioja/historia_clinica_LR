package net.pladema.errata.general.endpoints.controller;

import io.swagger.v3.oas.annotations.tags.Tag;

import net.pladema.errata.common.dto.ErrataRequestDTO;
import net.pladema.errata.common.repository.entity.Errata;
import net.pladema.errata.general.endpoints.service.ErrataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Errata", description = "Errata")
public class ErrataController {

	@Autowired
	private final ErrataService errataService;


    public ErrataController(ErrataService errataService) {
        this.errataService = errataService;
    }

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@PostMapping("institution/{institutionId}/errata/create")
	public ResponseEntity<?> createErrata(
			@RequestBody ErrataRequestDTO requestDTO, @PathVariable Integer institutionId
			) {
		try {
			Errata errata = errataService.createErrata(requestDTO);
			return ResponseEntity.ok(errata);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating errata with document ID " + requestDTO.getDocumentId());
		}
	}

	@PreAuthorize("hasPermission(#institutionId, 'ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ENFERMERO_ADULTO_MAYOR, ENFERMERO, ESPECIALISTA_EN_ODONTOLOGIA')")
	@GetMapping("institution/{institutionId}/document/{documentId}/errata")
	public ResponseEntity<?> getErrataByDocumentId(
			@PathVariable Integer institutionId, @PathVariable Integer documentId
	) {
		try {
			Errata errata = errataService.getErrataByDocumentId(documentId);
			if (errata != null) {
				return ResponseEntity.ok(errata);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving errata for document ID " + documentId);
		}
	}
}
