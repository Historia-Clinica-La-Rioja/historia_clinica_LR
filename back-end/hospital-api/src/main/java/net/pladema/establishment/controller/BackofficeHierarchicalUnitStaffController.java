package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeHierarchicalUnitStaffValidator;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.establishment.repository.entity.HierarchicalUnitStaff;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

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
@RequestMapping("backoffice/hierarchicalunitstaff")
public class BackofficeHierarchicalUnitStaffController extends AbstractBackofficeController<HierarchicalUnitStaff, Integer> {

	public BackofficeHierarchicalUnitStaffController(BackofficeHierarchicalUnitStaffStore store,
													 BackofficeHierarchicalUnitStaffValidator validator) {
		super(store, validator);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<HierarchicalUnitStaff> getList(Pageable pageable, HierarchicalUnitStaff entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<HierarchicalUnitStaff> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(hus -> itemsAllowed.ids.contains(hus.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
