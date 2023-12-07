package net.pladema.booking.controller;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import net.pladema.booking.repository.BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository;
import net.pladema.booking.repository.entity.BackofficeClinicalSpecialtyMandatoryMedicalPractice;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import net.pladema.sgx.exceptions.BackofficeValidationException;

@Component
public class BackofficeClinicalSpecialtyMandatoryMedicalPracticeValidator implements BackofficePermissionValidator<BackofficeClinicalSpecialtyMandatoryMedicalPractice, Integer> {

	private final BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository repository;


	public BackofficeClinicalSpecialtyMandatoryMedicalPracticeValidator(BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository repository) {
		this.repository = repository;
	}

	@Override
	public void assertGetList(BackofficeClinicalSpecialtyMandatoryMedicalPractice entity) {
	}

	@Override
	public List<Integer> filterIdsByPermission(List<Integer> ids) {
		return ids;
	}

	@Override
	public void assertGetOne(Integer id) {
	}

	@Override
	public void assertCreate(BackofficeClinicalSpecialtyMandatoryMedicalPractice entity) {
		assertNotExists(entity);
	}

	@Override
	public void assertUpdate(Integer id, BackofficeClinicalSpecialtyMandatoryMedicalPractice entity) {
	}

	@Override
	public void assertDelete(Integer id) {
	}

	@Override
	public ItemsAllowed itemsAllowedToList(BackofficeClinicalSpecialtyMandatoryMedicalPractice entity) {
		return new ItemsAllowed<>();
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		return null;
	}

	private void assertNotExists(BackofficeClinicalSpecialtyMandatoryMedicalPractice clinicalSpecialtyMandatoryMedicalPractice) {
		var csmmp = new BackofficeClinicalSpecialtyMandatoryMedicalPractice();
		csmmp.setClinicalSpecialtyId(clinicalSpecialtyMandatoryMedicalPractice.getClinicalSpecialtyId());
		csmmp.setMandatoryMedicalPracticeId(clinicalSpecialtyMandatoryMedicalPractice.getMandatoryMedicalPracticeId());
		var entity = repository.findOne(Example.of(csmmp));
		if (entity.isPresent()) {
			throw new BackofficeValidationException("clinical.specialty.mandatory.medical.practice.exists");
		}
	}
}
