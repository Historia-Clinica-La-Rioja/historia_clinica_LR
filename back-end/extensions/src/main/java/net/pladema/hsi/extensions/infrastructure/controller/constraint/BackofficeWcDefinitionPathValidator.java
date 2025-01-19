package net.pladema.hsi.extensions.infrastructure.controller.constraint;

import lombok.AllArgsConstructor;

import net.pladema.hsi.extensions.infrastructure.repository.WcDefinitionPathsRepository;
import net.pladema.hsi.extensions.infrastructure.repository.entities.ExtensionDefinitionPath;

import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;

import org.springframework.stereotype.Component;



@AllArgsConstructor
@Component
public class BackofficeWcDefinitionPathValidator implements BackofficeEntityValidator<ExtensionDefinitionPath, Short> {

	private final WcDefinitionPathsRepository repository;

	@Override
	public void assertCreate(ExtensionDefinitionPath entity) {
		assertName(entity);
		assertPath(entity);
	}

	@Override
	public void assertUpdate(Short id, ExtensionDefinitionPath entity) {
		assertName(entity);
		assertPath(entity);
	}

	@Override
	public void assertDelete(Short id) {

	}

	private void assertName(ExtensionDefinitionPath extensionDefinitionPath) {
		if (extensionDefinitionPath.getName() != null)
			if (repository.existsExtensionByName(extensionDefinitionPath.getName()))
				throw new BackofficeValidationException("Ya existe una extension con ese nombre.");
	}

	private void assertPath(ExtensionDefinitionPath extensionDefinitionPath) {
		if (extensionDefinitionPath.getPath() != null)
			if (repository.existsExtensionByPath(extensionDefinitionPath.getPath()))
				throw new BackofficeValidationException("Ya existe una extension con ese path.");
	}

}

