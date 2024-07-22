package net.pladema.establishment.controller;

import javax.validation.Valid;

import net.pladema.establishment.repository.entity.Bed;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeCareLineInstitutionValidator;
import net.pladema.establishment.repository.CareLineInstitutionRepository;
import net.pladema.establishment.repository.entity.CareLineInstitution;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("backoffice/carelineinstitution")
public class BackofficeCareLineInstitutionController extends AbstractBackofficeController<CareLineInstitution, Integer> {

	private final CareLineInstitutionRepository careLineInstitutionRepository;

	public BackofficeCareLineInstitutionController(CareLineInstitutionRepository repository,
												   BackofficeCareLineInstitutionValidator validator) {
		super(new BackofficeRepository<CareLineInstitution, Integer>(
				repository,
				new BackofficeQueryAdapter<CareLineInstitution>() {
					@Override
					public Example<CareLineInstitution> buildExample(CareLineInstitution entity) {
						return Example.of(entity);
					}
				}), validator);
		this.careLineInstitutionRepository = repository;
	}
	@Override
	public CareLineInstitution create(@Valid @RequestBody CareLineInstitution entity) {
		if(entity.getInstitutionId() == null || entity.getCareLineId() == null)
			throw new BackofficeValidationException("Debe completar todos los campos");
		boolean hasPersistedEntity = this.careLineInstitutionRepository
				.findByInstitutionIdAndCareLineId(entity.getInstitutionId(), entity.getCareLineId())
				.isPresent();
		if(hasPersistedEntity)
			throw new BackofficeValidationException("La asociación ya existe");
		return super.create(entity);
	}

	@Override
	@GetMapping(params = "!ids")
	public @ResponseBody Page<CareLineInstitution> getList(Pageable pageable, CareLineInstitution entity) {
		logger.debug("GET_LIST {}", entity);
		ItemsAllowed<Integer> itemsAllowed = permissionValidator.itemsAllowedToList(entity);
		if (itemsAllowed.all)
			return store.findAll(entity, pageable);

		List<CareLineInstitution> list = store.findAll(entity, PageRequest.of(0, Integer.MAX_VALUE, pageable.getSort()))
				.getContent()
				.stream()
				.filter(cli -> itemsAllowed.ids.contains(cli.getId()))
				.collect(Collectors.toList());

		int minIndex = pageable.getPageNumber()*pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
	}

}