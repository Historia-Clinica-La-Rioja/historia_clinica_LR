package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.medicalconsultation.diary.controller.dto.DiaryProtectedAppointmentsSearch;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableProtectedAppointmentsInfoBo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryAvailableProtectedAppointmentsSearchQuery;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class DiaryAvailableProtectedAppointmentsSearchRepositoryImpl implements DiaryAvailableProtectedAppointmentsSearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public DiaryAvailableProtectedAppointmentsSearchRepositoryImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	@Override
	public List<DiaryAvailableProtectedAppointmentsInfoBo> getAllDiaryProtectedAppointmentsByFilter(DiaryProtectedAppointmentsSearch diaryProtectedAppointmentsSearch) {
		DiaryAvailableProtectedAppointmentsSearchQuery diaryProtectedAppointmentsSearchQuery = new DiaryAvailableProtectedAppointmentsSearchQuery(diaryProtectedAppointmentsSearch);
		QueryPart queryPart = buildQuery(diaryProtectedAppointmentsSearchQuery, diaryProtectedAppointmentsSearch);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		return diaryProtectedAppointmentsSearchQuery.construct(query.getResultList());
	}

	private QueryPart buildQuery(DiaryAvailableProtectedAppointmentsSearchQuery appointmentsSearchQuery, DiaryProtectedAppointmentsSearch searchFilter){
		QueryPart queryPart = new QueryPart(
				"SELECT ")
				.concatPart(appointmentsSearchQuery.select())
				.concat(" FROM ")
				.concatPart(appointmentsSearchQuery.from())
				.concat(" WHERE ")
				.concatPart(appointmentsSearchQuery.where());
		return queryPart;
	}

}
