package net.pladema.hsi.extensions.infrastructure.controller;

import net.pladema.hsi.extensions.infrastructure.repository.WcDefinitionPathsRepository;
import net.pladema.hsi.extensions.infrastructure.repository.entities.ExtensionDefinitionPath;

import net.pladema.sgx.backoffice.rest.BackofficeEntityValidatorAdapter;

import net.pladema.sgx.backoffice.rest.NewAbstractBackofficeController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import static net.pladema.sgx.NewStoreBuilder.fromJpa;
import static net.pladema.sgx.backoffice.permissions.NewBackofficePermissionBuilder.permitAll;

@Controller
@RequestMapping("backoffice/wcDefinitionPath")
public class BackofficeWcDefinitionPathController extends NewAbstractBackofficeController<ExtensionDefinitionPath, Short> {

	public BackofficeWcDefinitionPathController(
			WcDefinitionPathsRepository repository
	) {
		super(
				fromJpa(repository),
				permitAll(),
				new BackofficeEntityValidatorAdapter<>()
		);
	}



}
