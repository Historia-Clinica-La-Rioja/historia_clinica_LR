package net.pladema.loinc.infrastructure.input.rest;

import java.util.List;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;

import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.loinc.infrastructure.output.entity.LoincCode;
import net.pladema.loinc.infrastructure.output.repository.LoincCodeRepository;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.backoffice.validation.BackofficeEntityValidator;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/loinc-codes")
public class BackofficeLoincCodeController extends AbstractBackofficeController<LoincCode, Integer> {
	public BackofficeLoincCodeController(LoincCodeRepository repository){
		super(
			new BackofficeRepository<>(
				repository,
				new BackofficeQueryAdapter<>(){
					@Override
					public Example<LoincCode> buildExample(LoincCode entity) {
						ExampleMatcher customExampleMatcher = ExampleMatcher
							.matching()
							.withMatcher("code", ExampleMatcher.GenericPropertyMatcher::startsWith)
							.withMatcher("description", x -> x.ignoreCase().contains())
							.withMatcher("customDisplayName", x -> x.ignoreCase().contains());
						return Example.of(entity, customExampleMatcher);
					}
				}
			),
			new BackofficePermissionValidator<LoincCode, Integer>() {
				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertGetList(LoincCode entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public List<Integer> filterIdsByPermission(List<Integer> ids) {return ids;}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertGetOne(Integer id) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertCreate(LoincCode entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertUpdate(Integer id, LoincCode entity) {}

				@Override
				@PreAuthorize("hasAnyAuthority('ROOT', 'ADMINISTRADOR')")
				public void assertDelete(Integer id) {}

				@Override
				public ItemsAllowed<Integer> itemsAllowedToList(LoincCode entity) {
					return new ItemsAllowed<>();
				}

				@Override
				public ItemsAllowed<Integer> itemsAllowedToList() {
					return new ItemsAllowed<>();
				}
			},
			new BackofficeEntityValidator<LoincCode, Integer>() {
				@Override
				public void assertCreate(LoincCode entity) {
					throw new BackofficeValidationException("loinc-code.create-disallowed");
				}

				@Override
				public void assertUpdate(Integer id, LoincCode entity) {
					var onlyAllowedFieldsChanged = repository
							.findById(id)
							.map(original -> entity.updateAllowed(original))
							.orElse(true);

					if (!onlyAllowedFieldsChanged)
						throw new BackofficeValidationException("loinc-code.editable-fields-disallowed");
				}

				@Override
				public void assertDelete(Integer id) {
					throw new BackofficeValidationException("loinc-code.delete-disallowed");
				}
			});
	}
}
