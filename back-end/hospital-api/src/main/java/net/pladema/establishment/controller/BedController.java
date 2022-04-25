package net.pladema.establishment.controller;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.establishment.controller.dto.*;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;

@RestController
@Tag(name = "Bed", description = "Bed")
@RequestMapping("/institution/{institutionId}/bed")
public class BedController {

	private static final Logger LOG = LoggerFactory.getLogger(BedController.class);

	private BedRepository bedRepository;

	private BedMapper bedMapper;

	private BedService bedService;

	public BedController(BedRepository bedRepository, BedMapper bedMapper, BedService bedService) {
		this.bedRepository = bedRepository;
		this.bedMapper = bedMapper;
		this.bedService = bedService;
	}

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<List<BedDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
		List<Bed> beds = bedRepository.getAllByInstitution(institutionId);
		LOG.debug("Get all Beds  => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}
	
	@GetMapping("/{bedId}/info")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<BedInfoDto> getBedInfo(@PathVariable(name = "institutionId") Integer institutionId, 
			@PathVariable(name = "bedId") Integer bedId) {
		Optional<BedInfoVo> bed = bedService.getBedInfo(bedId);
		LOG.debug("Get Bed summary  => {}", bed);
		return bed.isPresent() ? ResponseEntity.ok(bedMapper.toBedInfoDto(bed.get())) : 
				ResponseEntity.noContent().build();
	}

	@GetMapping("/summary-list")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO, ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<List<BedSummaryDto>> getNewBedSummaryDto(@PathVariable(name = "institutionId") Integer institutionId){
		LOG.debug("Input parameter -> institutionId {}", institutionId);
		List<BedSummaryVo> beds = bedService.getBedSummary(institutionId);
		LOG.trace("Output -> {}", beds);
		LOG.debug("Result size {}", beds.size());
		return ResponseEntity.ok(bedMapper.toListBedSummaryDto(beds));
	}

	@GetMapping("/clinicalspecialty/{clinicalSpecialtyId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_DE_CAMAS')")
	public ResponseEntity<List<BedDto>> getFreeBedsByClinicalSpecialty(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId) {
		List<Bed> beds = bedService.getFreeBeds(institutionId, clinicalSpecialtyId);
		LOG.debug("Get free Beds by ClinicalSpecialty response=> {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}
	
	

}
