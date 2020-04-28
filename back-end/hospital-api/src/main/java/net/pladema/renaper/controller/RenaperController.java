package net.pladema.renaper.controller;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import io.swagger.annotations.Api;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;
import net.pladema.renaper.controller.mapper.RenaperMapper;
import net.pladema.renaper.services.RenaperService;
import net.pladema.renaper.services.domain.PersonDataResponse;

@RestController
@RequestMapping("/renaper")
@Api(value = "Renaper", tags = { "Renaper" })
public class RenaperController {

	private static final String SEARCH_PERSON = "/searchPerson";

	private static final Logger LOG = LoggerFactory.getLogger(RenaperController.class);

	private static final String TIMEOUT_MSG = "Timeout en WS Renaper";

	private final RenaperService renaperService;

	private final RenaperMapper renaperMapper;

	@Value("${ws.renaper.request.timeout:10000}")
	private long requestTimeOut;

	public RenaperController(RenaperService renaperService, RenaperMapper renaperMapper) {
		super();
		this.renaperService = renaperService;
		this.renaperMapper = renaperMapper;
	}

	@GetMapping(value = SEARCH_PERSON)
	public DeferredResult<ResponseEntity<PersonBasicDataResponseDto>> getBasicPerson(
			@RequestParam(value = "identificationNumber", required = true) String identificationNumber,
			@RequestParam(value = "genderId", required = true) Short genderId) {
		LOG.debug("Input data -> identificationNumber: {} , genderId: {} ", identificationNumber, genderId);
		DeferredResult<ResponseEntity<PersonBasicDataResponseDto>> deferredResult = new DeferredResult<>(requestTimeOut);
		setCallbacks(deferredResult,SEARCH_PERSON);
		ForkJoinPool.commonPool().submit(() -> {
			Optional<PersonDataResponse> personData = renaperService.getPersonData(identificationNumber, genderId);
			if (!personData.isPresent()) {
				deferredResult.setResult(ResponseEntity.noContent().build());
			}
			PersonBasicDataResponseDto result = renaperMapper.fromPersonDataResponse(personData.get());
			deferredResult.setResult(ResponseEntity.ok().body(result));
		});
		return deferredResult;
	}

	private <R> void setCallbacks(DeferredResult<ResponseEntity<R>> deferredResult, String serviceName) {
		deferredResult.onTimeout(() -> {
			LOG.error("TimeOut en la invocación del servicio {}", serviceName);
			deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.SC_REQUEST_TIMEOUT).body(TIMEOUT_MSG));
		});
		deferredResult.onError(e -> {
			LOG.error("Error invocando {} ", serviceName);
			deferredResult
					.setErrorResult(ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(e.toString()));
		});
	}

}
