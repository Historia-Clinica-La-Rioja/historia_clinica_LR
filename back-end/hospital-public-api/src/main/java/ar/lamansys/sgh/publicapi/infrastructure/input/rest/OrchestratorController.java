package ar.lamansys.sgh.publicapi.infrastructure.input.rest;


import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.MoveResultDto;
import ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.SizeResultDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.imagenetwork.SharedLoadStudiesResultPort;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/orchestrator")
@Tag(name = "Orchestrator", description = "Orchestrator external Api")

public class OrchestratorController {

	private final SharedLoadStudiesResultPort moveStudiesService;

	@PostMapping("/update-result")
	public ResponseEntity<Boolean> updateResult(@RequestBody MoveResultDto moveResultDto){
		moveStudiesService.updateStatusAndResult(moveResultDto.getIdMove(), moveResultDto.getStatus(), moveResultDto.getResult());
		return ResponseEntity.ok().body(true);
	}

	@PostMapping("/set-size-study")
	public ResponseEntity<Boolean> setSizeStudy(@RequestBody SizeResultDto sizeResultDto){
		moveStudiesService.updateSize(sizeResultDto.getIdMove(), sizeResultDto.getSize(),  sizeResultDto.getImageId());
		return ResponseEntity.ok().body(true);
	}

}