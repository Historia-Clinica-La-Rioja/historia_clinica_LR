package ar.lamansys.sgh.publicapi.activities.infrastructure.input.rest;

import ar.lamansys.sgh.publicapi.activities.application.processactivity.ProcessActivity;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/public-api/institution/refset/{refsetCode}/activities/{activityId}/process")
@Tag(name = "PublicApi Facturacion", description = "Public Api activity process")
public class ProcessActivityController {

	private static final String INPUT = "Input data -> ";
	private final ProcessActivity processActivity;

	@PutMapping()
	public void markActivityAsProcessed(
			@PathVariable("refsetCode") String refsetCode,
			@PathVariable("activityId") Long activityId
	) {
		log.debug(INPUT + "refsetCode {}, activityId{}", refsetCode, activityId);
		processActivity.run(refsetCode, activityId);
	}
}
