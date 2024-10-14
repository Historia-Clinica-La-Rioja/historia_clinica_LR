package net.pladema.clinichistory.requests.servicerequests.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GetDiagnosticReportInfoRepositoryImpl implements GetDiagnosticReportInfoRepository {

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Object[] execute(Integer diagnosticReportId) {
        log.debug("Input parameters -> diagnosticReportId {}", diagnosticReportId);

        String sqlString = "SELECT " +
				"dr.id AS dr_id, " +
				"s.id, " +
				"s.sctid, " +
				"s.pt, " +
                "h.id AS hid, " +
				"h.s_id AS h_id, " +
				"h.pt AS h_pt, " +
				"h.sctid AS h_sctid, " +
                "n.description, " +
				"dr.status_id, " +
				"d.source_id, " +
				"dr.effective_time," +
                "d.created_by, " +
				"sr.observations " +
                "FROM {h-schema}diagnostic_report dr " +
                "JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
                "LEFT JOIN {h-schema}note n ON (dr.note_id = n.id) " +
                "JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
                "            FROM {h-schema}health_condition h1 " +
                "            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
                "          ) AS h ON (h.id = dr.health_condition_id) " +
                "JOIN {h-schema}document_diagnostic_report ddr ON (dr.id = ddr.diagnostic_report_id) " +
                "JOIN {h-schema}document d ON (d.id = ddr.document_id) " +
                "JOIN {h-schema}service_request sr ON (sr.id = d.source_id) " +
                "WHERE dr.id = :diagnosticReportId ";

        Query query = entityManager.createNativeQuery(sqlString);
        query.setParameter("diagnosticReportId", diagnosticReportId);
        List<Object[]> result = query.getResultList();
        return result.get(0);
    }

	@Transactional(readOnly = true)
	public Object[] getDiagnosticReportByAppointmentId(Integer apId) {
		log.debug("Input parameters -> apId {}", apId);

		String sqlString = "SELECT dr.id AS dr_id, s.id, s.sctid, s.pt, " +
				"h.id AS hid, h.s_id AS h_id, h.pt AS h_pt, h.sctid AS h_sctid, " +
				"n.description, dr.status_id, d.source_id, dr.effective_time," +
				"d.created_by " +
				"FROM {h-schema}diagnostic_report dr " +
				"JOIN {h-schema}snomed s ON (dr.snomed_id = s.id) " +
				"LEFT JOIN {h-schema}note n ON (dr.note_id = n.id) " +
				"JOIN {h-schema}appointment_order_image aoi ON (dr.id = aoi.study_id) " +
				"JOIN ( SELECT h1.id, s1.id as s_id, s1.pt, s1.sctid " +
				"            FROM {h-schema}health_condition h1 " +
				"            JOIN {h-schema}snomed s1 ON (h1.snomed_id = s1.id) " +
				"          ) AS h ON (h.id = dr.health_condition_id) " +
				"JOIN {h-schema}document_diagnostic_report ddr ON (dr.id = ddr.diagnostic_report_id) " +
				"JOIN {h-schema}document d ON (d.id = ddr.document_id) " +
				"WHERE aoi.appointment_id = :apId "+
				"AND aoi.order_id IS NOT null";
		Query query = entityManager.createNativeQuery(sqlString);
		query.setParameter("apId", apId);
		List<Object[]> result = query.getResultList();
		return (result.size() != 0 ? result.get(0) : null);
	}
}
