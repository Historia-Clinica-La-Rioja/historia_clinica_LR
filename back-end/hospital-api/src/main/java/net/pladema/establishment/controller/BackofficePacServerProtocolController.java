package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.PacServerProtocolRepository;
import net.pladema.establishment.repository.entity.PacServerProtocol;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/pacserverprotocols")
public class BackofficePacServerProtocolController extends AbstractBackofficeController<PacServerProtocol, Short> {

	public BackofficePacServerProtocolController(PacServerProtocolRepository repository) {
		super(repository);
	}
}
