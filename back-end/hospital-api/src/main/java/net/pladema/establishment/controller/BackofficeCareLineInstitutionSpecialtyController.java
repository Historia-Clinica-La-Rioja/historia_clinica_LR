package net.pladema.establishment.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.CareLineInstitutionSpecialtyRepository;
import net.pladema.establishment.repository.entity.CareLineInstitutionSpecialty;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@RestController
@RequestMapping("backoffice/carelineinstitutionspecialty")
public class BackofficeCareLineInstitutionSpecialtyController extends AbstractBackofficeController<CareLineInstitutionSpecialty, Integer> {

	private final CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository;

	public BackofficeCareLineInstitutionSpecialtyController(CareLineInstitutionSpecialtyRepository repository,
															CareLineInstitutionSpecialtyRepository careLineInstitutionSpecialtyRepository) {
		super(repository);
		this.careLineInstitutionSpecialtyRepository = careLineInstitutionSpecialtyRepository;
	}

	@Override
	public CareLineInstitutionSpecialty create(@Valid @RequestBody CareLineInstitutionSpecialty entity) {
		if(entity.getCareLineInstitutionId() == null || entity.getClinicalSpecialtyId() == null)
			throw new BackofficeValidationException("Debe completar todos los campos");
		boolean hasPersistedEntity = this.careLineInstitutionSpecialtyRepository
				.findByCareLineInstitutionIdAndClinicalSpecialtyId(entity.getCareLineInstitutionId(), entity.getClinicalSpecialtyId())
				.isPresent();
		if(hasPersistedEntity)
			throw new BackofficeValidationException("La especialidad ya fue agregada");
		return super.create(entity);
	}
}
