package net.pladema.medicalconsultation.doctorsoffice.controller;

import net.pladema.medicalconsultation.diary.repository.DiaryRepository;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;
import net.pladema.medicalconsultation.doctorsoffice.repository.DoctorsOfficeRepository;
import net.pladema.medicalconsultation.doctorsoffice.repository.entity.DoctorsOffice;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;

import net.pladema.sgx.backoffice.rest.SingleAttributeBackofficeQueryAdapter;
import net.pladema.sgx.exceptions.BackofficeValidationException;

import java.util.List;


public class DoctorsOfficeStore extends BackofficeRepository<DoctorsOffice, Integer> {

	private DiaryRepository diaryRepository;

	private DoctorsOfficeRepository repository;

	public DoctorsOfficeStore(DoctorsOfficeRepository repository, DiaryRepository diaryRepository) {
		super(repository, new SingleAttributeBackofficeQueryAdapter<DoctorsOffice>("description"));
		this.repository = repository;
		this.diaryRepository = diaryRepository;
	}

	@Override
	public void deleteById(Integer id) {
		assertDoctorsOfficeNoDiary(id);
		repository.deleteById(id);
	}

	private void assertDoctorsOfficeNoDiary(Integer id) {
		List<Diary> diaries = diaryRepository.findByDoctorsOfficeId(id, false);
		if (!diaries.isEmpty())
			throw new BackofficeValidationException("Existen agendas que referencian a éste consultorio");
	}

}
