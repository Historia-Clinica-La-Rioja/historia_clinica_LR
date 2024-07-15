package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.FetchActivityById;
import ar.lamansys.sgh.publicapi.activities.application.fetchactivitybyid.exceptions.ActivityNotFoundException;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto.AttentionInfoDto;
import ar.lamansys.sgh.publicapi.activities.infrastructure.input.mapper.ActivitiesMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/activities/{activityId}")
@Tag(name = "PublicApi Facturacion", description = "Public Api activity info")
public class ActivityInfoByInstitutionController {

	private final ActivitiesMapper activitiesMapper;
	private final FetchActivityById fetchActivityById;


	@GetMapping
	public @ResponseBody AttentionInfoDto getActivityByInstitution(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) throws ActivityNotFoundException {
		log.debug("refsetCode {}, activityId{}", refsetCode, activityId);

		var attention = fetchActivityById.run(refsetCode, activityId);
		AttentionInfoDto result = activitiesMapper.mapToAttentionInfoDto(attention);

		log.debug("refsetCode {}, activityId {} => {}", refsetCode, activityId, result);

		return result;
	}

}