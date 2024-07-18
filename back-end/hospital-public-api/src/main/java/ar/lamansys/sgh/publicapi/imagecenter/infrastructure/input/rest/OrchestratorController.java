package ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.PossibleStudies;
import ar.lamansys.sgh.publicapi.imagecenter.application.resultstudies.exceptions.ResultStudiesException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.PossibleStudiesDto;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.UpdateResult;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateResultException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.UpdateSize;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.exceptions.UpdateSizeException;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.MoveResultDto;
import ar.lamansys.sgh.publicapi.imagecenter.infrastructure.input.rest.dto.SizeResultDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PublicApi ImageCenter", description = "Orchestrator external Api")
@RequestMapping("/public-api/orchestrator")
public class OrchestratorController {

	private final UpdateResult updateResult;
	private final UpdateSize updateSize;

	private final PossibleStudies possibleStudies;

	@PostMapping("/update-result")
	public @ResponseBody Boolean
	updateResult(@RequestBody MoveResultDto moveResultDto) throws UpdateResultException {
		return updateResult.run(moveResultDto.getIdMove(), moveResultDto.getStatus(), moveResultDto.getResult());

	}

	@PostMapping("/set-size-study")
	public @ResponseBody Boolean
	setSizeStudy(@RequestBody SizeResultDto sizeResultDto) throws UpdateSizeException {
		return updateSize.run(sizeResultDto.getIdMove(), sizeResultDto.getSize(),  sizeResultDto.getImageId());
	}

	@PostMapping("/set-possible-studies")
	public @ResponseBody Boolean
	setPossibleStudies(@RequestBody PossibleStudiesDto possibleStudiesDto) throws ResultStudiesException {
		Integer idMove = possibleStudiesDto.getIdMove();
		Integer appointmentId = possibleStudiesDto.getAppointmentId();

		return possibleStudies.run(possibleStudiesDto.getIdMove(), possibleStudiesDto.getAppointmentId(), possibleStudiesDto.getStudies());
	}

}