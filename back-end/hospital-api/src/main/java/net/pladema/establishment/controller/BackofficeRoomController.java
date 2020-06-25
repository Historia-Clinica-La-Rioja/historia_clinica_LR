package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.RoomRepository;
import net.pladema.establishment.repository.entity.Room;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;

@RestController
@RequestMapping("backoffice/rooms")
public class BackofficeRoomController extends AbstractBackofficeController<Room, Integer> {

	public BackofficeRoomController(RoomRepository repository) {
		super(
				new BackofficeRepository<Room, Integer>(
				repository,
				new SingleAttributeBackofficeQueryAdapter<Room>("description")
				));
	}

}
