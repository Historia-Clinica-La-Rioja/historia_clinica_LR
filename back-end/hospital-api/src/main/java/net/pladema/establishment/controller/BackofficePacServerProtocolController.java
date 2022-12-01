package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.dto.PacServerProtocolDto;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/pacserverprotocols")
public class BackofficePacServerProtocolController extends AbstractBackofficeController<PacServerProtocolDto, Short> {

	public BackofficePacServerProtocolController(BackofficePacServerProtocolStore store) {
		super(store);
	}
}
