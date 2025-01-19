package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeClinicalServiceSectorValidator;
import net.pladema.establishment.infrastructure.output.entity.ClinicalSpecialtySector;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.staff.controller.mapper.BackofficeClinicalServiceSectorStore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/clinicalservicesectors")
public class BackofficeClinicalServiceSectorController extends AbstractBackofficeController<ClinicalSpecialtySector, Integer> {

	public BackofficeClinicalServiceSectorController(BackofficeClinicalServiceSectorStore repository,
													 BackofficeClinicalServiceSectorValidator backofficeClinicalSpecialtySectorValidator) {
		super(repository, backofficeClinicalSpecialtySectorValidator);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<ClinicalSpecialtySector> getList(Pageable pageable, ClinicalSpecialtySector entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<ClinicalSpecialtySector> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(css -> itemsAllowed.ids.contains(css.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
