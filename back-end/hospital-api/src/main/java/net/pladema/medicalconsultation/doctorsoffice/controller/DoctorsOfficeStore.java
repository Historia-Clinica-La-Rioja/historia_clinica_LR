package net.pladema.medicalconsultation.doctorsoffice.controller;

import net.pladema.emergencycare.repository.EmergencyCareEpisodeRepository;
import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;

import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import javax.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.List;


public class DoctorsOfficeStore extends BackofficeRepository<DoctorsOffice, Integer> {

	private DiaryRepository diaryRepository;

	private DoctorsOfficeRepository repository;

	private EmergencyCareEpisodeRepository emergencyCareEpisodeRepository;

	public DoctorsOfficeStore(DoctorsOfficeRepository repository, DiaryRepository diaryRepository, EmergencyCareEpisodeRepository emergencyCareEpisodeRepository) {
		super(repository, new SingleAttributeBackofficeQueryAdapter<DoctorsOffice>("description"));
		this.repository = repository;
		this.diaryRepository = diaryRepository;
		this.emergencyCareEpisodeRepository = emergencyCareEpisodeRepository;
	}

	@Override
	public void deleteById(Integer id) {
		assertDoctorsOfficeNoDiary(id);
		assertOcuppiedDoctorsOffice(id);
		repository.deleteById(id);
	}

	private void assertDoctorsOfficeNoDiary(Integer id) {
		List<Diary> diaries = diaryRepository.findByDoctorsOfficeId(id, false);
		if (!diaries.isEmpty())
			throw new BackofficeValidationException("Existen agendas que referencian a éste consultorio");
	}

	private void assertOcuppiedDoctorsOffice(Integer doctorsOfficeId) {
		if (emergencyCareEpisodeRepository.existsEpisodeInOffice(doctorsOfficeId, null) > 0)
			throw new BackofficeValidationException("doctorsoffices.existsIntermnmentOrEmergencyEpisode");
	}
}
