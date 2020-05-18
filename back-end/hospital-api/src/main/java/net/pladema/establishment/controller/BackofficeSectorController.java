package net.pladema.establishment.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

import net.pladema.error.controller.dto.ApiErrorMessage;
import net.pladema.establishment.repository.SectorRepository;
import net.pladema.establishment.repository.entity.Sector;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/sectors")
public class BackofficeSectorController extends AbstractBackofficeController<Sector, Integer> {

    private static final Map<String, String> constraintTocode;
    static {
        Map<String, String> codesMap = new HashMap<>();
        codesMap.put("uq_sector_description_institution_id", "sector-description-inst-unique");
        constraintTocode = Collections.unmodifiableMap(codesMap);
    }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex, WebRequest request) {
		String sqlError = NestedExceptionUtils.getMostSpecificCause(ex).getLocalizedMessage();
		//TODO: usar un ApiErrorMessage propio de backoffice
		String constraintCode = constraintTocode.getOrDefault(ex.getConstraintName(), "constraint-default");
		ApiErrorMessage apiErrors = new ApiErrorMessage(constraintCode, sqlError);
		return new ResponseEntity<>(apiErrors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
	
	public BackofficeSectorController(SectorRepository repository) {
		super(repository);
	}

}
