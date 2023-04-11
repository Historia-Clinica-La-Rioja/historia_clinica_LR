package net.pladema.nursingreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class QueryFactoryNR {

	@PersistenceContext
	private final EntityManager entityManager;

	public QueryFactoryNR(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<HospitalizationNursing> queryHospitalizationNursing(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59);

		Query query = entityManager.createNamedQuery("NursingReports.HospitalizationNursing");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<HospitalizationNursing> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<OutpatientNursing> queryOutpatientNursing(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("NursingReports.OutpatientNursing");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OutpatientNursing> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<NursingEmergencies> queryNursingEmergencies(Integer institutionId) {

		Query query = entityManager.createNamedQuery("NursingReports.NursingEmergencies");
		query.setParameter("institutionId", institutionId);
		List<NursingEmergencies> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<TotalNursingRecovery> queryTotalNursingRecovery(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(),start.getMonth(),start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("NursingReports.TotalNursingRecovery");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<TotalNursingRecovery> data = query.getResultList();
		return data;
	}

	@SuppressWarnings("unchecked")
	public List<VaccinesNursing> queryVaccinesNursing(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0, 0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23, 59, 59);

		Query query = entityManager.createNamedQuery("NursingReports.VaccinesNursing");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<VaccinesNursing> data = query.getResultList();
		return data;
	}
}
