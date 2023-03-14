package net.pladema.establishment.controller;

import javax.validation.Valid;

import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;

import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.controller.constraints.validator.permissions.BackofficeCareLineInstitutionValidator;
import net.pladema.establishment.repository.CareLineInstitutionRepository;
import net.pladema.establishment.repository.entity.CareLineInstitution;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

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

}