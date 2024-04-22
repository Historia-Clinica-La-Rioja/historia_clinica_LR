package net.pladema.provincialreports.epidemiologyreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository
public class EpidemiologyReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public EpidemiologyReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<DenguePatientControlConsultationDetail> queryDenguePatientControl(Integer institutionId) {

		Query query = entityManager.createNamedQuery("EpidemiologyReports.DenguePatientControlConsultationDetail");
		query.setParameter("institutionId", institutionId);
		List<DenguePatientControlConsultationDetail> data = query.getResultList();
		return data;

	}

}
