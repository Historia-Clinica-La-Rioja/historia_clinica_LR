package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeRoomValidator;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/rooms")
public class BackofficeRoomController extends AbstractBackofficeController<Room, Integer> {

	public BackofficeRoomController(RoomRepository repository, BackofficeRoomValidator backofficeRoomValidator) {
		super(
				new BackofficeRepository<>(
				repository,
						new BackofficeQueryAdapter<Room>() {
							@Override
							public Example<Room> buildExample(Room entity) {
								ExampleMatcher matcher = ExampleMatcher
										.matching()
										.withMatcher("description", x -> x.ignoreCase().contains())
										.withMatcher("roomNumber", x -> x.ignoreCase().contains())
										.withMatcher("type", x -> x.ignoreCase().contains());
								return Example.of(entity, matcher);
							}
						}), backofficeRoomValidator);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<Room> getList(Pageable pageable, Room entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<Room> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(r -> itemsAllowed.ids.contains(r.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}
