package net.pladema.establishment.controller;

import ar.lamansys.sgx.shared.masterdata.domain.EnumWriter;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.establishment.controller.dto.AttentionPlacesQuantityDto;
import net.pladema.establishment.controller.dto.RoomDto;
import net.pladema.establishment.controller.dto.SectorDto;
import net.pladema.establishment.controller.mapper.RoomMapper;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.establishment.service.SectorBOMapper;
import net.pladema.establishment.service.SectorService;
import net.pladema.establishment.service.domain.AttentionPlacesQuantityBo;
import net.pladema.establishment.service.domain.ESectorType;
import net.pladema.establishment.service.domain.SectorBO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@Tag(name = "Sector", description = "Sector")
@RequestMapping("/institution/{institutionId}/sector")
@RequiredArgsConstructor
@Slf4j
public class SectorController {
	
	private final SectorRepository sectorRepository;
	private final RoomRepository roomRepository;
	private final RoomMapper roomMapper;
	private final SectorService sectorOfTypeService;
	private final SectorBOMapper sectorBOMapper;

	@GetMapping
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<Sector>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
		List<Sector> sectors = sectorRepository.getSectorsByInstitution(institutionId);
		log.debug("Get all Sectors => {}", sectors);
		return ResponseEntity.ok(sectors);
	}
	
	@GetMapping("/{sectorId}/specialty/{specialtyId}/rooms")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRATIVO_RED_DE_IMAGENES')")
	public ResponseEntity<List<RoomDto>> getAllRoomsBySectorAndSpecialty(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorId") Integer sectorId,
			@PathVariable(name = "specialtyId") Integer specialtyId) {
		List<Room> rooms = roomRepository.getAllBySectorAndInstitution(sectorId, specialtyId, institutionId);
		log.debug("Get all Rooms => {}", rooms);
		return ResponseEntity.ok(roomMapper.toListRoomDto(rooms));
	}


	@GetMapping("/sectoroftype/{sectorTypeId}")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA')")
	public ResponseEntity<List<SectorDto>>  getAllOfType(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "sectorTypeId") Short sectorTypeId) {
		List<SectorBO> sectors = sectorOfTypeService.getSectorOfType(institutionId,sectorTypeId );
		List<SectorDto> result = sectorBOMapper.toListSectorDto(sectors);
		log.debug("Get all Sectors of Type => {}", sectors);
		return ResponseEntity.ok(result);
	}

	@GetMapping(value = "/sectortype")
	public ResponseEntity<Collection<MasterDataDto>> getSectorType() {
		log.debug("{}", "All Sector types");
		return ResponseEntity.ok().body(EnumWriter.writeList(ESectorType.getAll()));
	}

	@GetMapping(value = "/attentionPlaces/{sectorTypeId}")
	public ResponseEntity<AttentionPlacesQuantityDto> quantityAttentionPlacesBySectorType(@PathVariable(name = "institutionId") Integer institutionId,
																@PathVariable(name = "sectorTypeId") Short sectorTypeId) {
		log.debug("Input parameters -> institutionId {}, sectorTypeId {}", institutionId, sectorTypeId);
		AttentionPlacesQuantityBo result = sectorOfTypeService.quantityAttentionPlacesBySectorType(institutionId, sectorTypeId);
		return ResponseEntity.ok().body(new AttentionPlacesQuantityDto(result.getShockroom(), result.getDoctorsOffice(), result.getBed()));
	}
}
