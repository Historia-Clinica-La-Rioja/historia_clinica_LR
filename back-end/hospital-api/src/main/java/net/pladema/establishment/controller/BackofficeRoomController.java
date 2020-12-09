package net.pladema.establishment.controller;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeRoomValidator;
import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

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

}
