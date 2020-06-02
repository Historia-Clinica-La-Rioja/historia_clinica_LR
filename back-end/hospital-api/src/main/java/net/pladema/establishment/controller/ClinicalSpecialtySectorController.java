package net.pladema.establishment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.repository.ClinicalSpecialtySectorRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;

@RestController
@Api(value = "ClinicalSpecialtySector", tags = { "Clinical Specialty Sector" })
@RequestMapping("/sector/{sectorId}/clinicalspecialty")
public class ClinicalSpecialtySectorController  {

	private static final Logger LOG = LoggerFactory.getLogger(ClinicalSpecialtySectorController.class);
	
	private ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository;
	
	public ClinicalSpecialtySectorController(ClinicalSpecialtySectorRepository clinicalSpecialtySectorRepository) {
		this.clinicalSpecialtySectorRepository = clinicalSpecialtySectorRepository;
	}

	@GetMapping()
	public ResponseEntity<List<ClinicalSpecialty>> getAllSpecialtyBySector(@PathVariable(name = "sectorId") Integer sectorId){
		List<ClinicalSpecialty> clinicalSpecialties = clinicalSpecialtySectorRepository.getAllBySector(sectorId);
		LOG.debug("Get all Clinical Specialty by Sector {} => {}", sectorId, clinicalSpecialties);
		return ResponseEntity.ok(clinicalSpecialties);
	}
}
