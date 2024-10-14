package net.pladema.establishment.controller;

import net.pladema.clinichistory.hospitalization.controller.externalservice.InternmentEpisodeExternalService;
import net.pladema.emergencycare.service.EmergencyCareEpisodeService;
import net.pladema.establishment.controller.constraints.validator.entities.BackofficeBedEntityValidator;
import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeBedValidator;
import net.pladema.establishment.repository.BedRepository;
import net.pladema.establishment.repository.entity.Bed;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
								   BackofficeBedValidator backofficeBedValidator,
								   EmergencyCareEpisodeService emergencyCareEpisodeService) {
		super(new BackofficeRepository<Bed, Integer>(
						repository,
						new SingleAttributeBackofficeQueryAdapter<Bed>("bedNumber")
				), backofficeBedValidator, new BackofficeBedEntityValidator(internmentEpisodeExternalService, emergencyCareEpisodeService));
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<Bed> getList(Pageable pageable, Bed entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<Bed> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(bed -> itemsAllowed.ids.contains(bed.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}


}
