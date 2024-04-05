package net.pladema.establishment.controller;

import javax.validation.Valid;

import net.pladema.establishment.repository.CareLineInstitutionRepository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Synchronized;
import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;
import net.pladema.establishment.repository.entity.CareLineInstitutionPractice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.dto.BackofficeDeleteResponse;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/carelineinstitutionpractice")
public class BackofficeCareLineInstitutionPracticesController extends AbstractBackofficeController<CareLineInstitutionPractice, Integer> {

	private final CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository;

	private final CareLineInstitutionRepository careLineInstitutionRepository;

	public BackofficeCareLineInstitutionPracticesController(CareLineInstitutionPracticeRepository repository,
															CareLineInstitutionRepository careLineInstitutionRepository) {
		super(repository);
		this.careLineInstitutionPracticeRepository = repository;
		this.careLineInstitutionRepository = careLineInstitutionRepository;
	}

	@Override
	public CareLineInstitutionPractice create(@Valid @RequestBody CareLineInstitutionPractice entity) {
		boolean careLineAcceptPractices = careLineInstitutionRepository.careLineAcceptPractices(entity.getCareLineInstitutionId());
		if (!careLineAcceptPractices)
			throw new BackofficeValidationException("La línea de cuidado no abarca procedimientos");
		if(entity.getCareLineInstitutionId() == null || entity.getSnomedRelatedGroupId() == null)
			throw new BackofficeValidationException("Debe completar todos los campos");
		boolean hasPersistedEntity = this.careLineInstitutionPracticeRepository.
				findByCareLineInstitutionIdAndSnomedRelatedGroupId(entity.getCareLineInstitutionId(), entity.getSnomedRelatedGroupId())
				.isPresent();
		if(hasPersistedEntity)
			throw new BackofficeValidationException("La práctica ya fue agregada");
		return super.create(entity);
	}

	@Override
	@Synchronized
	public BackofficeDeleteResponse<Integer> delete(@PathVariable("id") Integer id) {
		return super.delete(id);
	}
}
