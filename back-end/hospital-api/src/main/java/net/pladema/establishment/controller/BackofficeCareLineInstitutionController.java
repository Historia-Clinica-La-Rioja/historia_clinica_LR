package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
												   BackofficeCareLineInstitutionValidator validator,
												   CareLineInstitutionRepository careLineInstitutionRepository) {
		super(repository, validator);
		this.careLineInstitutionRepository = careLineInstitutionRepository;
	}

	@Override
	public Page<CareLineInstitution> getList(Pageable pageable, CareLineInstitution entity) {
		int minIndex = pageable.getPageNumber() * pageable.getPageSize();
		int maxIndex = minIndex + pageable.getPageSize();
		var list = super.getList(pageable, entity).getContent();
		return new PageImpl<>(list.subList(minIndex, Math.min(maxIndex, list.size())), pageable, list.size());
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