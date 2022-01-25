package net.pladema.establishment.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@Tag(name = "Clinical specialty sector", description = "Clinical specialty sector")
@RequestMapping("/institution/{institutionId}/sector/{sectorId}/clinicalspecialty")
public class ClinicalSpecialtySectorController {

	private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtySectorController.class);

	private ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository;

	public ClinicalSpecialtySectorController(ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository) {
		this.clinicalSpecialtySectorRepository = clinicalSpecialtySectorRepository;
	}

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<ClinicalSpecialty>> getAllSpecialtyBySector(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorId") Integer sectorId) {
		List<ClinicalSpecialty> clinicalSpecialties = clinicalSpecialtySectorRepository
				.getAllBySectorAndInstitution(sectorId, institutionId);
		clinicalSpecialties.forEach(ClinicalSpecialty::fixSpecialtyType);
		LOG.debug("Get all Clinical Specialty by Sector {} and institution {} => {}", sectorId, institutionId,
				clinicalSpecialties);
		return ResponseEntity.ok(clinicalSpecialties);
	}
}
