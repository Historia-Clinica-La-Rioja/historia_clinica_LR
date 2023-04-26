package net.pladema.medicalconsultation.shockroom.infrastructure.controller;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.BackofficeShockroomStore;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.ShockroomRepository;
import net.pladema.medicalconsultation.shockroom.infrastructure.repository.entity.Shockroom;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/shockroom")
public class BackofficeShockroomController extends AbstractBackofficeController<Shockroom, Integer> {

	public BackofficeShockroomController(ShockroomRepository repository) {
		super(new BackofficeShockroomStore(repository));
	}
}
