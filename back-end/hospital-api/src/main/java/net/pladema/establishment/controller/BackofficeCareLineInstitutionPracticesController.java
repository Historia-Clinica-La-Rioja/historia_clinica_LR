package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.CareLineInstitutionPracticeRepository;
import net.pladema.establishment.repository.entity.CareLineInstitutionPractice;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/carelineinstitutionpractice")
public class BackofficeCareLineInstitutionPracticesController extends AbstractBackofficeController<CareLineInstitutionPractice, Integer> {

	private final CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository;

	public BackofficeCareLineInstitutionPracticesController(CareLineInstitutionPracticeRepository repository,
															CareLineInstitutionPracticeRepository careLineInstitutionPracticeRepository) {
		super(repository);
		this.careLineInstitutionPracticeRepository = careLineInstitutionPracticeRepository;
	}

	@Override
	public CareLineInstitutionPractice create(@Valid @RequestBody CareLineInstitutionPractice entity) {
		if(entity.getCareLineInstitutionId() == null || entity.getSnomedRelatedGroupId() == null)
			return null;
		boolean hasPersistedEntity = this.careLineInstitutionPracticeRepository.
				findByCareLineInstitutionIdAndSnomedRelatedGroupId(entity.getCareLineInstitutionId(), entity.getSnomedRelatedGroupId())
				.isPresent();
		if(hasPersistedEntity)
			return null;
		return super.create(entity);
	}
}
