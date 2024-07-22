package net.pladema.establishment.controller;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeRootSectorValidator;
import net.pladema.establishment.repository.RootSectorRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.establishment.repository.entity.RootSector;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

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
@RequestMapping("backoffice/rootsectors")
public class BackofficeRootSectorController extends AbstractBackofficeController<RootSector, Integer> {

	public BackofficeRootSectorController(BackofficeRootSectorStore store, BackofficeRootSectorValidator sectorValidator) {
		super(store, sectorValidator);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<RootSector> getList(Pageable pageable, RootSector entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<RootSector> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(rs -> itemsAllowed.ids.contains(rs.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
