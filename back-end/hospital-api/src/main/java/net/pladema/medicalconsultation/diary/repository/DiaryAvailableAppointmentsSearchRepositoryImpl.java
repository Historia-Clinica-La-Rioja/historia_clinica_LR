package net.pladema.medicalconsultation.diary.repository;

import ar.lamansys.sgx.shared.repositories.QueryPart;
import net.pladema.medicalconsultation.diary.domain.DiaryAppointmentsSearchBo;
import net.pladema.medicalconsultation.diary.service.domain.DiaryAvailableAppointmentsInfoBo;
import net.pladema.medicalconsultation.diary.repository.domain.DiaryAvailableAppointmentsSearchQuery;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class DiaryAvailableAppointmentsSearchRepositoryImpl implements DiaryAvailableAppointmentsSearchRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public DiaryAvailableAppointmentsSearchRepositoryImpl(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	@Override
	public List<DiaryAvailableAppointmentsInfoBo> getAllDiaryAppointmentsByFilter(DiaryAppointmentsSearchBo diaryAppointmentsSearchBo) {
		DiaryAvailableAppointmentsSearchQuery diaryAppointmentsSearchQuery = new DiaryAvailableAppointmentsSearchQuery(diaryAppointmentsSearchBo);
		QueryPart queryPart = buildQuery(diaryAppointmentsSearchQuery);
		Query query = entityManager.createNativeQuery(queryPart.toString());
		queryPart.configParams(query);
		return diaryAppointmentsSearchQuery.construct(query.getResultList());
	}

	private QueryPart buildQuery(DiaryAvailableAppointmentsSearchQuery appointmentsSearchQuery){
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
