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

import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.establishment.controller.dto.RoomDto;
import net.pladema.establishment.controller.mapper.BedMapper;
import net.pladema.establishment.controller.mapper.RoomMapper;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.establishment.repository.entity.Room;

@RestController
@Tag(name = "Room", description = "Room")
@RequestMapping("/institution/{institutionId}/room")
public class RoomController {

	private static final Logger LOG = LoggerFactory.getLogger(RoomController.class);

	private RoomRepository roomRepository;

	private RoomMapper roomMapper;

	private BedRepository bedRepository;

	private BedMapper bedMapper;

	public RoomController(RoomRepository roomRepository, RoomMapper roomMapper, BedRepository bedRepository,
			BedMapper bedMapper) {
		this.roomRepository = roomRepository;
		this.roomMapper = roomMapper;
		this.bedRepository = bedRepository;
		this.bedMapper = bedMapper;
	}

	@GetMapping()
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO, ADMINISTRADOR_AGENDA, ESPECIALISTA_MEDICO, ENFERMERO, PROFESIONAL_DE_SALUD, ESPECIALISTA_EN_ODONTOLOGIA')")
	public ResponseEntity<List<RoomDto>> getAll(@PathVariable(name = "institutionId") Integer institutionId) {
		List<Room> rooms = roomRepository.getAllByInstitution(institutionId);
		LOG.debug("Get all Rooms => {}", rooms);
		return ResponseEntity.ok(roomMapper.toListRoomDto(rooms));
	}

	@GetMapping("/{roomId}/beds")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<BedDto>> getAllBedsByRoom(@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "roomId") Integer roomId) {
		List<Bed> beds = bedRepository.getAllByRoomAndInstitution(roomId, institutionId);
		LOG.debug("Get all Beds => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}

	@GetMapping("/{roomId}/freebeds")
	@PreAuthorize("hasPermission(#institutionId, 'ADMINISTRATIVO')")
	public ResponseEntity<List<BedDto>> getAllFreeBedsByRoom(
			@PathVariable(name = "institutionId") Integer institutionId,
			@PathVariable(name = "roomId") Integer roomId) {
		List<Bed> beds = bedRepository.getAllFreeBedsByRoomAndInstitution(roomId, institutionId);
		LOG.debug("Get all free Beds  => {}", beds);
		return ResponseEntity.ok(bedMapper.toListBedDto(beds));
	}
}
