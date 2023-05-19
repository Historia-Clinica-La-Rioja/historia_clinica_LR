package net.pladema.establishment.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import io.swagger.v3.oas.annotations.tags.Tag;
import net.pladema.clinichistory.hospitalization.service.InternmentEpisodeService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.dto.PatientBedRelocationDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.repository.entity.HistoricPatientBedRelocation;
import net.pladema.establishment.service.BedService;

@RestController
@Tag(name = "Patient bed relocation", description = "Patient bed relocation")
@RequestMapping("/institution/{institutionId}")
public class PatientBedRelocationController {

	private static final Logger LOG = LoggerFactory.getLogger(PatientBedRelocationController.class);

	private BedMapper bedMapper;

	private BedService bedService;

	private InternmentEpisodeService internmentEpisodeService;
	public PatientBedRelocationController(BedMapper bedMapper, BedService bedService, InternmentEpisodeService internmentEpisodeService) {
		this.bedMapper = bedMapper;
		this.bedService = bedService;
		this.internmentEpisodeService = internmentEpisodeService;
	}


	@PostMapping("/bed/relocation")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<PatientBedRelocationDto> addPatientBedRelocation(@PathVariable(name = "institutionId") Integer institutionId,
			@RequestBody PatientBedRelocationDto patientBedRelocationDto) {
		LOG.debug("Add bed relocation => {}", patientBedRelocationDto);
		Optional<HistoricPatientBedRelocation> historic = bedService.getLastPatientBedRelocation(patientBedRelocationDto.getInternmentEpisodeId());

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		LocalDateTime relocationDate = LocalDateTime.parse(patientBedRelocationDto.getRelocationDate(),dateTimeFormatter);
		LocalDateTime entryDate = historic.map(HistoricPatientBedRelocation::getRelocationDate)
				.orElse(internmentEpisodeService.getEntryDate(patientBedRelocationDto.getInternmentEpisodeId()));
		boolean canBeRelocated = entryDate.isBefore(relocationDate);
		if (!canBeRelocated) {
			return ResponseEntity.noContent().build();
		}
		HistoricPatientBedRelocation result = bedService.addPatientBedRelocation(bedMapper.fromPatientBedRelocationDto(patientBedRelocationDto));
		LOG.debug("Add bed relocation result => {}", result);
		return ResponseEntity.ok(bedMapper.toPatientBedRelocationDto(result));
	}

	@GetMapping("/internment/{internmentEpisodeId}/bed/relocation/last")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<PatientBedRelocationDto> getLastPatientBedRelocation(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "internmentEpisodeId") Integer internmentEpisodeId) {
		Optional<HistoricPatientBedRelocation> historicPatientBedRelocation = bedService.getLastPatientBedRelocation(internmentEpisodeId);
		if (historicPatientBedRelocation.isPresent()) {
			LOG.debug("Get Last PatientBedRelocation by InternmentEpisodeId response=> {}", historicPatientBedRelocation.get());
			return ResponseEntity.ok(bedMapper.toPatientBedRelocationDto(historicPatientBedRelocation.get()));
		} else return ResponseEntity.noContent().build();
	}

}
