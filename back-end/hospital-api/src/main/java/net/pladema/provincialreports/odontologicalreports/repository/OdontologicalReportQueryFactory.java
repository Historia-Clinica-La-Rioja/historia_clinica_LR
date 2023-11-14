package net.pladema.provincialreports.odontologicalreports.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class OdontologicalReportQueryFactory {

	@PersistenceContext
	private final EntityManager entityManager;

	public OdontologicalReportQueryFactory(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<OdontologyConsultationDetail> queryPromocionPrimerNivel(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.PromocionPrimerNivelConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologyConsultationDetail> queryPrevencionPrimerNivel(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.PrevencionPrimerNivelConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologyConsultationDetail> queryPrevencionGrupalPrimerNivel(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.PrevencionGrupalPrimerNivelConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologyConsultationDetail> queryOperatoriaSegundoNivel(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.OperatoriaSegundoNivelConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologyConsultationDetail> queryEndodonciaSegundoNivel(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.EndodonciaSegundoNivelConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologyConsultationDetail> data = query.getResultList();
		return data;

	}

	@SuppressWarnings("unchecked")
	public List<OdontologicalProceduresConsultationDetail> queryOdontologicalProcedures(Integer institutionId, LocalDate start, LocalDate end) {

		var startDate = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(), 0,0);
		var endDate = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), 23,59,59, LocalTime.MAX.getNano());

		Query query = entityManager.createNamedQuery("OdontologicalReports.OdontologicalProceduresConsultationDetail");
		query.setParameter("institutionId", institutionId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<OdontologicalProceduresConsultationDetail> data = query.getResultList();
		return data;

	}

}
