package net.pladema.establishment.controller;

import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.establishment.controller.constraints.validator.entities.BackofficeBedEntityValidator;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeBedValidator;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("backoffice/beds")
public class BackofficeBedController extends AbstractBackofficeController<Bed, Integer> {
	
	private static final Map<String, String> constraintTocode;
    static {
        Map<String, String> codesMap = new HashMap<>();
        codesMap.put("ch_bed_enabled_available", "beds.enabled-available");
        codesMap.put("ch_bed_available_free", "beds.available-free");
        constraintTocode = Collections.unmodifiableMap(codesMap);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
		String sqlError = NestedExceptionUtils.getMostSpecificCause(ex).getLocalizedMessage();
		String constraintCode = constraintTocode.getOrDefault(ex.getConstraintName(), "constraint-default");
		ApiErrorMessageDto apiErrors = new ApiErrorMessageDto(constraintCode, sqlError);
		return new ResponseEntity<>(apiErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	public BackofficeBedController(BedRepository repository,
								   InternmentEpisodeExternalService internmentEpisodeExternalService,
								   BackofficeBedValidator backofficeBedValidator) {
		super(new BackofficeRepository<Bed, Integer>(
						repository,
						new SingleAttributeBackofficeQueryAdapter<Bed>("bedNumber")
				), backofficeBedValidator, new BackofficeBedEntityValidator(internmentEpisodeExternalService));
	}

}
