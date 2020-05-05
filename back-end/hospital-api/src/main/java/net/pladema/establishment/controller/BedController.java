package net.pladema.establishment.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.Bed;

@RestController
@Api(value = "Bed", tags = { "Bed" })
@RequestMapping("/bed")
public class BedController {

	private static final Logger LOG = LoggerFactory.getLogger(BedController.class);

	private BedRepository bedRepository;

	private BedMapper bedMapper;

	public BedController(BedRepository bedRepository, BedMapper bedMapper) {
		this.bedRepository = bedRepository;
		this.bedMapper = bedMapper;
	}

	@GetMapping()
	public ResponseEntity<List<BedDto>> getAll() {
		List<Bed> beds = bedRepository.findAll();
		LOG.debug("Get all Beds  => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}

}
