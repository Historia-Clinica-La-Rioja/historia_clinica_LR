package net.pladema.establishment.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.dto.BedInfoDto;
import net.pladema.establishment.controller.dto.BedSummaryDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.domain.BedInfoVo;
import net.pladema.establishment.repository.domain.BedSummaryVo;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.service.BedService;

@RestController
@Api(value = "Bed", tags = { "Bed" })
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
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<BedDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
		List<Bed> beds = bedRepository.getAllByInstitucion(institutionId);
		LOG.debug("Get all Beds  => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}
	
	@GetMapping("/{bedId}/info")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO')")
	public ResponseEntity<BedInfoDto> getBedInfo(@PathVariable(name = "institutionId") Integer institutionId, 
			@PathVariable(name = "bedId") Integer bedId) {
		Optional<BedInfoVo> bed = bedService.getBedInfo(bedId);
		LOG.debug("Get Bed summary  => {}", bed);
		return bed.isPresent() ? ResponseEntity.ok(bedMapper.toBedInfoDto(bed.get())) : 
				ResponseEntity.noContent().build();
	}

	@GetMapping("/summary-list")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ENFERMERO')")
	public ResponseEntity<List<BedSummaryDto>> getBedSummaryList(@PathVariable(name = "institutionId") Integer institutionId){ 
		List<BedSummaryVo> beds = bedService.getBedSummary(institutionId);
		LOG.debug("Get Bed summary  => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedSummaryDto(beds)); 
	}

	@GetMapping("/clinicalspecialty/{clinicalSpecialtyId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<BedDto>> getFreeBedsByClinicalSpecialty(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "clinicalSpecialtyId") Integer clinicalSpecialtyId) {
		List<Bed> beds = bedService.getFreeBeds(institutionId, clinicalSpecialtyId);
		LOG.debug("Get free Beds by ClinicalSpecialty response=> {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}
	
	

}
