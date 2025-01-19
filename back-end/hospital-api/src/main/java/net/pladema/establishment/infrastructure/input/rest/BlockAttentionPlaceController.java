package net.pladema.establishment.infrastructure.input.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.pladema.establishment.application.attentionplaces.BlockAttentionPlace;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;
import net.pladema.establishment.infrastructure.input.rest.dto.BlockAttentionPlaceCommandDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/institution/{institutionId}/attention-places")
@RestController
public class BlockAttentionPlaceController {

	private final BlockAttentionPlace blockAttentionPlace;

	@PostMapping("/block/bed/{bedId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void blockBed(
		@PathVariable(name = "institutionId") Integer institutionId,
		@PathVariable(name = "bedId") Integer bedId,
		@RequestBody BlockAttentionPlaceCommandDto blockCommandDto
	){
		log.debug("Block bed -> institutionId {}, bedId {}", institutionId, bedId);
		blockAttentionPlace.blockBed(
			institutionId,
			bedId,
			EBlockAttentionPlaceReason.map(blockCommandDto.getReasonId()),
			blockCommandDto.getReason());
	}

	@PostMapping("/block/shockroom/{shockroomId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void blockShockRoom(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "shockroomId") Integer shockroomId,
			@RequestBody BlockAttentionPlaceCommandDto blockCommandDto
	){
		log.debug("Block shockroom -> institutionId {}, shockroomId {}", institutionId, shockroomId);
		blockAttentionPlace.blockShockRoom(
				institutionId,
				shockroomId,
				EBlockAttentionPlaceReason.map(blockCommandDto.getReasonId()),
				blockCommandDto.getReason());
	}

	@PostMapping("/block/doctors-office/{doctorsOfficeId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void blockdoctorsOffice(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "doctorsOfficeId") Integer doctorsOfficeId,
			@RequestBody BlockAttentionPlaceCommandDto blockCommandDto
	){
		log.debug("Block doctorsOffice -> institutionId {}, doctorsOfficeId {}", institutionId, doctorsOfficeId);
		blockAttentionPlace.blockdoctorsOffice(
				institutionId,
				doctorsOfficeId,
				EBlockAttentionPlaceReason.map(blockCommandDto.getReasonId()),
				blockCommandDto.getReason());
	}

	@PutMapping("/unblock/bed/{bedId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void unblockBed(
		@PathVariable(name = "institutionId") Integer institutionId,
		@PathVariable(name = "bedId") Integer bedId
	){
		log.debug("Unblock bed -> institutionId {}, bedId {}", institutionId, bedId);
		blockAttentionPlace.unblockBed(institutionId, bedId);
	}

	@PutMapping("/unblock/shockroom/{shockroomId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void unblockShockRoom(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "shockroomId") Integer shockroomId
	){
		log.debug("Unblock Shockroom -> institutionId {}, shockroomId {}", institutionId, shockroomId);
		blockAttentionPlace.unblockShockRoom(institutionId, shockroomId);
	}

	@PutMapping("/unblock/doctors-office/{doctorsOfficeId}")
	@PreAuthorize("hasPermission(#institutionId, 'ENFERMERO, ESPECIALISTA_MEDICO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public void unblockDoctorsOffice(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "doctorsOfficeId") Integer doctorsOfficeId
	){
		log.debug("Unblock doctors office -> institutionId {}, doctorsOfficeId {}", institutionId, doctorsOfficeId);
		blockAttentionPlace.unblockDoctorsOffice(institutionId, doctorsOfficeId);
	}

}
