package net.pladema.unitofmeasure.insfrastructure.input.rest;


import net.pladema.loinc.infrastructure.output.entity.LoincCode;
import net.pladema.sgx.backoffice.exceptions.PermissionDeniedException;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import net.pladema.unitofmeasure.insfrastructure.output.entity.UnitOfMeasure;

import net.pladema.unitofmeasure.insfrastructure.output.repository.UnitOfMeasureRepository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("backoffice/units-of-measure")
public class BackofficeUnitOfMeasureController extends AbstractBackofficeController<UnitOfMeasure, Short> {

	public BackofficeUnitOfMeasureController(UnitOfMeasureRepository repository) {
		super(
			new BackofficeRepository<>(
				repository,
				new BackofficeQueryAdapter<>(){
					@Override
					public Example<UnitOfMeasure> buildExample(UnitOfMeasure entity) {
						ExampleMatcher customExampleMatcher = ExampleMatcher
								.matching()
								.withMatcher("code", ExampleMatcher.GenericPropertyMatcher::contains);
						return Example.of(entity, customExampleMatcher);
					}
				}
			),
			new BackofficePermissionValidator<UnitOfMeasure, Short>() {
				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertGetList(UnitOfMeasure entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public List<Short> filterIdsByPermission(List<Short> ids) {
					return ids;
				}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertGetOne(Short id) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertCreate(UnitOfMeasure entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertUpdate(Short id, UnitOfMeasure entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertDelete(Short id) {
					throw new PermissionDeniedException("No se permite eliminar unidades de estudio");
				}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public ItemsAllowed<Short> itemsAllowedToList(UnitOfMeasure entity) {
					return new ItemsAllowed<>();
				}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public ItemsAllowed<Short> itemsAllowedToList() {
					return new ItemsAllowed<>();
				}
			}, new BackofficeEntityValidator<UnitOfMeasure, Short>() {
				@Override
				public void assertCreate(UnitOfMeasure entity) {

				}

				@Override
				public void assertUpdate(Short id, UnitOfMeasure entity) {
					var onlyAllowedFiledsChanged = repository
					.findById(id)
					.map(original -> entity.updateAllowed(original))
					.orElse(true);

					if (!onlyAllowedFiledsChanged)
						throw new BackofficeValidationException("unit-of-measure.editable-fields.disallowed");
				}

				@Override
				public void assertDelete(Short id) {}
			});
	}
}
